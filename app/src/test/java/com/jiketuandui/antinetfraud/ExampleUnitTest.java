package com.jiketuandui.antinetfraud;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        getComment comment = new getComment();
        String str = comment.postComment("username=testUser&token=$2y$10$HodAY9rOT93TeLJnOgeyjODJb4Ocsfyc895qZGmtwuuqgfyvKi.fe&phone_id=null&article_id=&user_id=3&content=162");
//                comment.postComment(
//                        "username=testUser" +
//                                "&token=$2y$10$HodAY9rOT93TeLJnOgeyjODJb4Ocsfyc895qZGmtwuuqgfyvKi.fe" +
//                                "&phone_id=18:dc:56:a0:68:1e" +
//                                "&article_id=162" +
//                                "&user_id=3" +
//                                "&content=这是一个评论"
//                );
        System.out.println("输出字符：str = " + str + "，长度为：length = " + str.length());
        //System.out.println(comment.getCommented("162"));
    }
}