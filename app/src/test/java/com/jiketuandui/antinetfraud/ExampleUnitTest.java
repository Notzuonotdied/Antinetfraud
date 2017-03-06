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
        //System.out.printf(String.valueOf(getConnect.setPraise(String.valueOf(73))));
        //System.out.printf(getConnect.setContentPost("1","微信").get(0).getId());
        System.out.printf(String.valueOf(getConnect.doPraiseGet("http://119.29.220.221/?/api/praise/73")));
    }
}