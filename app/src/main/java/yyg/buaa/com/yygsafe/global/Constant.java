package yyg.buaa.com.yygsafe.global;

/**
 * Created by yyg on 2016/4/14.
 */
public class Constant {

    public interface BlackNumber {
        /**
         * 黑名单的拦截模式
         * 0 不拦截
         * 1 拦截所有
         * 2 只拦截短信
         * 3 只拦截电话
         */
        String MODE_NONE = "0";
        String MODE_ALL = "1";
        String MODE_SMS = "2";
        String MODE_PHONE = "3";

        /**
         * 黑名单的数据库名
         */
        String DATABASE = "callsafe.db";

        /**
         * 黑名单的数据库表名
         */
        String TABLE = "blackinfo";
    }

}
