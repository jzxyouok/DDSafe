package yyg.buaa.com.yygsafe.global;

/**
 * Created by yyg on 2016/4/14.
 */
public class BlackNumber {
    /**
     * 黑名单的拦截模式
     * 0 不拦截
     * 1 拦截所有
     * 2 只拦截短信
     * 3 只拦截电话
     */
    private static final String MODE_NONE = "0";
    private static final String MODE_ALL = "1";
    private static final String MODE_SMS = "2";
    private static final String MODE_PHONE = "3";

    /**
     * 黑名单的数据库名
     */
    private static final String DATABASE = "callsafe.db";

    /**
     * 黑名单的数据库表名
     */
    private static final String TABLE = "blackinfo";
}
