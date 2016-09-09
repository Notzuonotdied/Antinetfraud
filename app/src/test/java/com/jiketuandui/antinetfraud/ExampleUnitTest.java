package com.jiketuandui.antinetfraud;

import android.content.Context;

import com.jiketuandui.antinetfraud.Activity.MainActivity;
import com.jiketuandui.antinetfraud.Bean.ArticleContent;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.HTTP.getConnect;
import com.jiketuandui.antinetfraud.Util.getWindowsWidth;

import org.junit.Test;

import java.util.List;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        //assertEquals(4, 2 + 2);

        /**
         * 测试通过
         * */
//        String str= getConnect.doGet("http://127.0.0.1/?/api/article_list/1/2/1");
//        System.out.println(str);


//        List<ListContent> newListContents = getConnect.setContentURL(getConnect.UrlContentHead,"1");
//        if (newListContents.isEmpty()) {
//            System.out.println("空");
//        }
//        for (ListContent listContent : newListContents) {
//            System.out.println("id=" + listContent.getId() + ",imageLink=" +
//                    listContent.getImagelink());
            //System.out.println(listContent.toString());
//        }
//        System.out.println("size:" + newListContents.size());


//        ArticleContent articleContent = getConnect.setArticleURL(88);
//        System.out.println("size:" + articleContent.getTags().size());

//        String str = getConnect.doPost("http://127.0.0.1/?/api/search", "神灵武士");
//        System.out.println(str);

    }
}