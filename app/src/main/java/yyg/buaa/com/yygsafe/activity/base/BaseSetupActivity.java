package yyg.buaa.com.yygsafe.activity.base;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import yyg.buaa.com.yygsafe.R;

/**
 * Created by yyg on 2016/4/9.
 */
public abstract class BaseSetupActivity extends BaseActivity{

    private GestureDetector detector;

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        //初始化手势识别器
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            //e1    代表手指第一次触摸屏幕的事件
            //e2    代表手指离开屏幕一瞬间的事件
            //velocityX 水平方向的速度 单位 pix/s
            //velocityY 竖直方向的速度
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                if (Math.abs(velocityX) < 200) {
                    Toast.makeText(getApplicationContext(), "速度太慢了", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {
                    Toast.makeText(getApplicationContext(), "竖直滑动无效", Toast.LENGTH_SHORT).show();
                    return true;
                }

                //从左向右滑动，显示上一个
                if (e2.getRawX() - e1.getRawX() > 200) {
                    showPre();
                    overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
                    return true;
                }

                //从右向左滑动，显示下一个
                if (e1.getRawX() - e2.getRawX() > 200) {
                    showNext();
                    overridePendingTransition(R.anim.next_in, R.anim.next_out);
                    return true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    //用手势识别器去识别事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //分析手势事件
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void progressClick(View v) {

    }

    /**
     * 系统调用，按钮点击下一步监听事件
     * @param v
     */
    public void next(View v) {
        showNext();
        overridePendingTransition(R.anim.next_in, R.anim.next_out);
    }

    /**
     * 系统调用，按钮点击上一步监听事件
     * @param v
     */
    public void pre(View v) {
        showPre();
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
    }

    public abstract void showNext();
    public abstract void showPre();


}
