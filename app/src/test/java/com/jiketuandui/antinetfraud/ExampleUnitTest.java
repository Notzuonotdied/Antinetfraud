package com.jiketuandui.antinetfraud;

import com.jiketuandui.antinetfraud.HTTP.postAccount;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
    postAccount pc = new postAccount();
    System.out.println(pc.postFeedback("content=1234567890pioiuytreeww"));
}
}