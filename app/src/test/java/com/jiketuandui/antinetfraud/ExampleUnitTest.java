package com.jiketuandui.antinetfraud;

import com.jiketuandui.antinetfraud.HTTP.getAnnouncement;
import com.jiketuandui.antinetfraud.HTTP.getPraise;
import com.jiketuandui.antinetfraud.HTTP.postShareContent;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        //System.out.printf(String.valueOf(instanceConnect.setPraise(String.valueOf(73))));
        //System.out.printf(instanceConnect.setContentPost("1","微信").get(0).getId());
        //System.out.printf(String.valueOf(instanceConnect.doPraiseGet("http://119.29.220.221/?/api/praise/73")));
//        postShareContent postSC = new postShareContent();
//        System.out.printf(String.valueOf(
//                postSC.post("title=Test&&type=1&&content=111~"))
//        );
        getAnnouncement postSC = new getAnnouncement();
        System.out.printf(String.valueOf(
                postSC.getAnnounce("2"))
        );
    }
}