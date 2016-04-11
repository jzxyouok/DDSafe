package yyg.buaa.com.yygsafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.service.LocationService;

/**
 * Created by yyg on 2016/4/10.
 */
public class SmsReceiver extends BroadcastReceiver{
    private static final String TAG = "SmsReceiver";
    private DevicePolicyManager dpm;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "收到短信了");
        //获取超级管理员
        dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object pdu : pdus) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
            String sender = sms.getOriginatingAddress();
            String message = sms.getMessageBody();
            if ("#*location*#".equals(message)) {
                //发送位置信息
                Log.i(TAG, "正在发送位置信息");
                //开启服务查询位置发送短信
                context.startService(new Intent(context, LocationService.class));
                abortBroadcast();

            } else if ("#*alarm*#".equals(message)) {
                //发送铃声
                Log.i(TAG, "正在发送铃声");
                MediaPlayer play = MediaPlayer.create(context, R.raw.bzj);
                play.setVolume(1.0f, 1.0f);
                play.setLooping(true);
                play.start();
                abortBroadcast();

            } else if ("#*wipedata*#".equals(message)) {
                //清除数据
                Log.i(TAG, "正在清除数据");
                dpm.wipeData(0);    //清除数据
                dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);    //清除包括sd卡在内的所有数据
                abortBroadcast();

            } else if ("#*lockscreen*#".equals(message)) {
                //远程锁屏
                Log.i(TAG, "正在远程锁屏");
                dpm.lockNow();  //锁屏
                dpm.resetPassword("123", 0); //设置锁屏密码
                abortBroadcast();

            }
        }
    }


}
