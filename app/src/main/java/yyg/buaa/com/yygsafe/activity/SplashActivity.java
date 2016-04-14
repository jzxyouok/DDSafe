package yyg.buaa.com.yygsafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.activity.base.BaseActivity;
import yyg.buaa.com.yygsafe.utils.StreamUtils;
import yyg.buaa.com.yygsafe.utils.UIUtils;

public class SplashActivity extends BaseActivity {

    private static final int LOAD_MAINUI = 1;
    private static final int SHOW_UPDATE_DIALOG = 2;
    private TextView tv_splash_version;
    private PackageManager packManager;
    private int versionCodeService;
    private String downloadurl;
    private String desc;
    private int versionCodeClient;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_MAINUI:
                    startActivityAndFinishSelf(HomeActivity.class);
                    break;
                case SHOW_UPDATE_DIALOG:
                    //因为对话框是activity的一部分，显示对话框必须指定activity的环境（令牌）
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle("更新提醒");
                    builder.setMessage(desc);
//                    builder.setCancelable(false);
                    builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //下载
                            download(downloadurl);

                        }
                    });
                    builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                             //进入主界面
                            startActivityAndFinishSelf(HomeActivity.class);
                        }
                    });
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //进入主界面
                            startActivityAndFinishSelf(HomeActivity.class);
                        }
                    });
                    builder.show();
                    break;

            }
            return false;
        }
    });
    private RelativeLayout rl_splash_root;

    @Override
    public void initView() {
        setContentView(R.layout.activity_splash);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
    }

    @Override
    public void initData() {
        String versionName = getPackageInfo().versionName;
        versionCodeClient = getPackageInfo().versionCode;
        tv_splash_version.setText(versionName);

        //splash渐变动画
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(2000);
        rl_splash_root.startAnimation(aa);

        //检查版本
        if (sp.getBoolean("autoupdate", false)) {
            checkVersion();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        startActivityAndFinishSelf(HomeActivity.class);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void initListener() {
    }

    @Override
    public void progressClick(View v) {
    }

    /**
     * 使用xUtils3下载apk
     * @param downloadurl
     */
    private void download(final String downloadurl) {
        RequestParams params = new RequestParams(downloadurl);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath("/sdcard/temp.apk");
        x.http().get(params, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                //成功下载
                // 安装apk
                //<action android:name="android.intent.action.VIEW" />
                //<category android:name="android.intent.category.DEFAULT" />
                //<data android:scheme="content" />
                //<data android:scheme="file" />
                //<data android:mimeType="application/vnd.android.package-archive" />
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.apk")),
                        "application/vnd.android.package-archive");
                startActivityForResult(intent, 0);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //下载失败
                startActivityAndFinishSelf(HomeActivity.class);
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        startActivityAndFinishSelf(HomeActivity.class);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public PackageInfo getPackageInfo() {
        try {
            packManager = getPackageManager();
            PackageInfo packInfo = packManager.getPackageInfo(getPackageName(), 0);
            return packInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            //不会发生   can't reach
            return null;
        }
    }

    /**
     * 连接服务器，检查版本号，看是否有更新
     */
    public void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                //检查代码执行的时间，如果时间少于2秒则补足2秒
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL(getResources().getString(R.string.serverurl));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(2000);
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream(); //json文本
                        String json = StreamUtils.readFromStream(is);
                        if (TextUtils.isEmpty(json)) {
                            //服务器json获取失败
                            UIUtils.showToast(SplashActivity.this, "错误2013，服务器json获取失败，请联系客服");
                            msg.what = LOAD_MAINUI;
                        } else {
                            JSONObject jsonObj = new JSONObject(json);
                            versionCodeService = jsonObj.getInt("version");
                            downloadurl = jsonObj.getString("downloadurl");
                            desc = jsonObj.getString("desc");
                            if (versionCodeService == versionCodeClient) {
                                //服务器版本和本地版本一致，进入UI主界面
                                msg.what = LOAD_MAINUI;
                            } else {
                                //不同，弹出更新提醒对话框
                                msg.what = SHOW_UPDATE_DIALOG;
                            }
                        }
                    } else {
                        //服务器状态码错误
                        UIUtils.showToast(SplashActivity.this, "错误2014，服务器状态码错误，请联系客服");
                        msg.what = LOAD_MAINUI;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //错误2011，网络错误，请联系客服
                    UIUtils.showToast(SplashActivity.this, "错误2011，网络错误，请联系客服");
                    msg.what = LOAD_MAINUI;
                } catch (JSONException e) {
                    e.printStackTrace();
                    //错误2012，json解析错误
                    UIUtils.showToast(SplashActivity.this, "错误2012，json解析错误");
                    msg.what = LOAD_MAINUI;
                } finally {
                    long endTime = System.currentTimeMillis();
                    long dTime = endTime - startTime;
                    if (dTime < 2000) {
                        //少于2秒，补足2秒
                        try {
                            Thread.sleep(2000 - dTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
}
