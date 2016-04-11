package yyg.buaa.com.yygsafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by yyg on 2016/4/10.
 */
public class LocationService extends Service {

    private static final String TAG = "LocationService";
    private LocationManager lm;
    private MyListener listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "LocationService服务开启了");
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);   //获得好的准确度
        criteria.setCostAllowed(true);  //允许花费

        //criteria查询条件
        //true 只返回可用的位置提供者
        String provider = lm.getBestProvider(criteria, true);

        listener = new MyListener();
        lm.requestLocationUpdates(provider, 0 , 0, listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(listener); //为了节省性能,当服务销毁时,删除位置更新的服务
        listener = null;
    }

    class MyListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //获取位置信息
            StringBuffer sb = new StringBuffer();
            sb.append("Accuracy" + location.getAccuracy() + "\n");
            sb.append("Speed" + location.getSpeed() + "\n");
            sb.append("Latitude"+ location.getLatitude() + "\n");
            sb.append("Longitude" + location.getLongitude() + "\n");
            String result = sb.toString();
            Log.i(TAG, result);
            //发送短信
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            SmsManager.getDefault().sendTextMessage(sp.getString("safeNumber", ""),
                    null, result, null, null);

            //关闭服务
            stopSelf();
        }

        //位置提供者发生改变
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        //位置提供者可用
        @Override
        public void onProviderEnabled(String provider) {
        }

        //位置提供者不可用
        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
