package yyg.buaa.com.yygsafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import yyg.buaa.com.yygsafe.db.dao.BlackNumberDAO;
import yyg.buaa.com.yygsafe.global.Constant;

/**
 * Created by yyg on 2016/4/20.
 */
public class CallSmsSafeReceiver extends BroadcastReceiver {

    public static final String TAG = "Receiver";
    private BlackNumberDAO dao;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "CallSmsSafeReceiver: 短信到来了。");
        dao = new BlackNumberDAO(context);
        //判断短信的发件人是否在黑名单中
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String sender = smsMessage.getOriginatingAddress();
            String mode = dao.queryMode(sender);
            if (Constant.BlackNumber.MODE_ALL.equals(mode) || Constant.BlackNumber.MODE_SMS.equals(mode)) {
                Log.i(TAG, "CallSmsSafeReceiver: 黑名单短信被拦截。");
                abortBroadcast();   //拦截广播
            }

            //智能拦截。
            String body = smsMessage.getMessageBody();
            if(body.contains("发票")){ //你的头发票亮极了。
                Log.i(TAG, "CallSmsSafeReceiver: 拦截到垃圾发票短信，终止");
                abortBroadcast();//终止短信的广播 ，短信就被拦截
            }
        }
    }
}
