package com.jiketuandui.antinetfraud.Util;

import com.jiketuandui.antinetfraud.HTTP.getImage;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by Notzuonotdied on 2016/9/11.
 * 缓存清理
 */
public class CacheCleanManage {
    /**
     * 清除Image Cache
     */
    public static boolean CleanImageCache() {
        return deleteDir(new File(getImage.photoPath));
    }

    /**
     * 删除文件
     *
     * @param dir 文件路径
     */
    private static boolean deleteDir(File dir) {
        if (dir==null) {
            return true;
        }
        // dir是一个目录就返回true，不是就返回false
        if (dir.isDirectory()) {
            for (String children : dir.list()) {
                if (!deleteDir(new File(dir, children))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 获取文件夹的大小
     */
    public static long getFolderSize(File file) {
        long size = 0;

        try {
            // 获取文件夹下的文件
            File[] fileLists = file.listFiles();

            for (File mFile : fileLists) {
                size = size + (mFile.isDirectory() ? getFolderSize(mFile) : mFile.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化缓存的单位
     * */
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraByte = gigaByte / 1024;
        if (teraByte < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }

        BigDecimal result4 = new BigDecimal(teraByte);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 获取缓存的大小
     * */
    public static String getCacheSize(File file) {
        return getFormatSize(getFolderSize(file));
    }
}
