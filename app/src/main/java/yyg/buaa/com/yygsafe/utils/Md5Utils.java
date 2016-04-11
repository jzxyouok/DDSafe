package yyg.buaa.com.yygsafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yyg on 2016/4/8.
 */
public class Md5Utils {

    public static String encode(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] result = digest.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                int i = b % 0xff;   //将字节转为整数
                String hexString = Integer.toHexString(i);
                if (hexString.length() == 1) {
                    hexString = "0" + hexString;    //如果长度等于1，加0补位
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            //can't reach
            e.printStackTrace();
            return "";
        }
    }
}
