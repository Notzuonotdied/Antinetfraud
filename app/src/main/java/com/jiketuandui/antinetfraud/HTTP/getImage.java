package com.jiketuandui.antinetfraud.HTTP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.jiketuandui.antinetfraud.Bean.ArticleContent;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Notzuonotdied on 2016/8/15.
 * <p>
 * 获取头图
 */
public class getImage {
    private static final String TAG = "Notzuonotdied";
    /**
     * The constant photoPath.
     */
    private static String photoPath = Environment.getExternalStorageDirectory()
            .getPath() + "/AntiNETFraud/images/";

    /**
     * Gets bitmap.
     *
     * @param mArticleContent 案例对象
     * @param reWidth         要求的宽度
     * @param reHeight        要求的高度
     * @param context         context
     * @return the bitmap 返回图片Bitmap
     */
    public static Bitmap getBitmap(ArticleContent mArticleContent,
                                   int reWidth, int reHeight, Context context) {
        if (!mArticleContent.getImagelink().equals("")) {
            return getBitmapFromEX(downloadIntoFile(mArticleContent.getImagelink(),
                    mArticleContent.getId()), reWidth, reHeight);
        } else {
            return getDefaultBitmap(reWidth, reHeight, context);
        }
    }

    /**
     * 获取默认图片
     *
     * @param reWidth  要求的宽度
     * @param reHeight 要求的高度
     * @param context  context
     * @return the bitmap 返回图片Bitmap
     */
    private static Bitmap getDefaultBitmap(int reWidth, int reHeight, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = getTheBestinSampleSize(options, reWidth, reHeight);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.loading),
                null, options);
    }

    /**
     * 批量获取图片
     *
     * @param mListContents 要批量获取图片的List集合
     * @param reWidth       要求的宽度
     * @param reHeight      要求的高度
     * @return the bitmap
     */
    public static List<ListContent> getBitmap(List<ListContent> mListContents, int reWidth,
                                              int reHeight, Context context) {
        if (mListContents != null) {
            int size = mListContents.size();
            for (int i = 0; i < size; i++) {
                ListContent temp = mListContents.get(i);
                if (temp.getImagelink() != null) {
                    mListContents.get(i).setmBitmap(getBitmapFromEX(
                            downloadIntoFile(temp.getImagelink(), temp.getId()),
                            reWidth, reHeight));
                } else {
                    mListContents.get(i).setmBitmap(getDefaultBitmap(reWidth, reHeight, context));
                }
            }
        }
        return mListContents;
    }

    /**
     * 获取图片
     *
     * @param fileName 图片缓存的地址
     * @param reWidth  要求的宽度
     * @param reHeight 要求的高度
     */
    private static Bitmap getBitmapFromEX(String fileName, int reWidth,
                                          int reHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);
        options.inSampleSize = getTheBestinSampleSize(options, reWidth, reHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(fileName, options);
    }

    /**
     * 获取最佳的缩放值
     *
     * @param options  BitmapFactory.Options
     * @param reWidth  要求的宽度
     * @param reHeight 要求的高度
     */
    private static int getTheBestinSampleSize(BitmapFactory.Options options,
                                              int reWidth, int reHeight) {
        // Math.round四舍五入取整
        return options.outWidth > reWidth || options.outHeight > reHeight ?
                Math.min(Math.round((float) options.outWidth / (float) reWidth),
                        Math.round((float) options.outHeight / (float) reHeight))
                : 1;
    }

    /**
     * 下载图片，并且将图片保存到内存卡中
     *
     * @param reImageLink 要求的图片的网址
     * @param reId        要求的图片的Id
     */
    private static String downloadIntoFile(String reImageLink, String reId) {
        String imageLink = getConnect.myUrl + reImageLink;
        Environment.getExternalStoragePublicDirectory(photoPath);
        File fileDirectory = new File(photoPath);
        if (!fileDirectory.exists()) {
            if (fileDirectory.mkdirs()) {
                Log.i(TAG, "创建文件夹成功！");
            }
        }
        File tempFile = new File(photoPath, reId);
        FileInputStream fis = null;
        if (tempFile.exists()) {
            try {
                fis = new FileInputStream(tempFile);
                try {
                    if (fis.available() == 0) {
                        if (tempFile.delete()) {
                            Log.i(TAG, "文件删除成功！");
                        }
                    } else {
                        return photoPath + reId;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (tempFile.createNewFile()) {
                Log.i(TAG, "新建成功！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 流
        InputStream is = null;
        FileOutputStream fileOutputStream = null;

        try {
            URL url = new URL(imageLink);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-type", "application/x-java-serialized-object");
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();

            fileOutputStream = new FileOutputStream(tempFile);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, len);
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return photoPath + reId;
    }
}
