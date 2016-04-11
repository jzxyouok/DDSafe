package yyg.buaa.com.yygsafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by yyg on 2016/4/10.
 */
public class BootCompleteReceiver extends BroadcastReceiver{

    private static final String TAG = "BootCompleteReceiver";
    private SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        if (sp.getBoolean("protecting", false)) {
            //防盗保护开启,判断sim卡是否变化
            Log.i(TAG, "防盗保护已开启，正在判断sim是否变化");
            String bindSim = sp.getString("sim", null);     //绑定的sim
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String realSim = tm.getSimSerialNumber();
            if (!bindSim.equals(realSim)) {
                //sim卡发生变化，发送短信
                Log.i(TAG, "sim卡发生变化，正在发送短信");
                String phone = sp.getString("safeNumber", "");
                SmsManager manager = SmsManager.getDefault();
                manager.sendTextMessage(phone, null, "sim Changed!", null, null);
            } else {
                //sim没有发生变化，输出log
                Log.i(TAG, "sim没有发生变化");
            }
        } else {
            //防盗保护未开启，不发送短信
            Log.i(TAG, "防盗保护未开启，不发送短信");
        }
    }
}
