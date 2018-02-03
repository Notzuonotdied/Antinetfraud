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
}