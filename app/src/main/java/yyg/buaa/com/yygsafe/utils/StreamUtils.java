package yyg.buaa.com.yygsafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by xx on 2016/4/6.
 */
public class StreamUtils {

    /**
     * 把一个流里的内容转换成一个字符串
     * @param is    输入流
     * @return
     */
    public static String readFromStream(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            String result = baos.toString();
            baos.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
