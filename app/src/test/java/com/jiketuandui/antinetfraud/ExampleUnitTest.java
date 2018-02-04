package com.jiketuandui.antinetfraud;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncryptUtils;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        String test = "��ظv���\u001E���\u0002Y�b�7<+��oY�K\u0003�\u000F�(��\u000F�Fm\u007F��V\u0012j�^�e���|ƃ;�+)u��-�\u0006V\u0015�30\u0014K�\u007FQ\u0010ܫ�v�sk���t��y��3����\u001F�e/s�q�?\u0017��r���Bx�˺\u0004��oe�;}V�\t\"W\u0003��e�@��\u0015Z{\n";
        byte[] string = EncryptUtils.decryptAES(test.getBytes(),
                ConvertUtils.hexString2Bytes("23337613555654242333761355565424"),
                "AES/ECB/PKCS5Padding", null);
        System.out.println(new String(string));
    }

    @Test
    public void testEncrypt() {
        String temp = "神灵巫师神灵巫师神灵巫师神灵巫师神灵巫师神灵巫师神灵巫师";
        byte[] what = EncryptUtils.encryptAES(temp.getBytes(),
                ConvertUtils.hexString2Bytes("23337613555654242333761355565424"),
                "AES/ECB/PKCS5Padding", null);
        System.out.println(new String(what));
        byte[] string = EncryptUtils.decryptAES(what, ConvertUtils.hexString2Bytes("11111111111111111111111111111111"),
                "AES/ECB/PKCS5Padding", null);
        System.out.println(new String(string));
    }

    @Test
    public void testMD5() {
        String token = "$2y$10$Z0Luo8AsXcjTBLhQQgmJAuvfgvp/BBqcfUFg4nUM.MaHwK.bohQaG";
        int articleId = 168;
        long millis = (long) 1517709688642.;
        String auth = "auth/article/collection";
        String sign = EncryptUtils.encryptMD5ToString(token + millis + auth);
        if ("76877DA205EC0A73F1B875279A726309".equals(sign)) {
            System.out.println("匹配");
            System.out.println(sign.length());
        }
    }

    @Test
    public void testMD52() {
        String string = "$2y$10$Z0Luo8AsXcjTBLhQQgmJAuvfgvp/BBqcfUFg4nUM.MaHwK.bohQaG1517722706auth/article/collection";
        String sign = EncryptUtils.encryptMD5ToString(string);
        if ("26cbdf9db5eb972dc340b0c196c14bb3".equals(sign)) {
            System.out.println("小写匹配");
            System.out.println(sign.length());
        } else if ("26CBDF9DB5EB972DC340B0C196C14BB3".equals(sign)) {
            System.out.println("大写匹配");
            System.out.println(sign.length());
        }
    }
}