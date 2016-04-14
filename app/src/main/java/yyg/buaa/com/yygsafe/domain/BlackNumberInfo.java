package yyg.buaa.com.yygsafe.domain;

/**
 * Created by yyg on 2016/4/14.
 */
public class BlackNumberInfo {
    /**
     * 黑名单的号码
     */
    private String number;
    /**
     * 黑名单的拦截模式
     * 0 不拦截
     * 1 拦截所有
     * 2 只拦截短信
     * 3 只拦截电话
     */
    private String mode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        if ("1".equals(mode) || "2".equals(mode) || "3".equals(mode)) {
            //匹配拦截成功
            this.mode = mode;
        } else {
            this.mode = "0";
        }
    }
}
