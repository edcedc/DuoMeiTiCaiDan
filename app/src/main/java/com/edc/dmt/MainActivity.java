package com.edc.dmt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.edc.dmt.mar.MyApplication;
import com.edc.dmt.service.RestartAppService;
import com.edc.dmt.service.killSelfService;
import com.edc.dmt.ui.HomeFrg;
import com.mrwang.stacklibrary.RootActivity;
import com.mrwang.stacklibrary.RootFragment;

public class MainActivity extends RootActivity {

    private killSelfService connServiceconn = new killSelfService();

    @NonNull
    @Override
    protected RootFragment getRootFragment() {
        //当此窗口为用户可见时，保持设备常开，并保持亮度不变。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
       // 隐藏EdiText键盘自动弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //设置当前窗体为全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 实际上下面两个方法都是在 BaseActivity 中做
        ((MyApplication) getApplication()).addActivity(this);

        ServiceUtils.bindService(RestartAppService.class, connServiceconn, BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.restart.RECEIVER");
        registerReceiver(reStartReceiver, filter);
        return new HomeFrg();
    }

    private BroadcastReceiver reStartReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.example.restart.RECEIVER")) {
                finish();
                LogUtils.e("zoulema");
                ServiceUtils.stopService(RestartAppService.class);
                ServiceUtils.unbindService(connServiceconn);

                Intent i = getPackageManager() .getLaunchIntentForPackage(getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }
    };

    /**
     * Android 点击EditText文本框之外任何地方隐藏键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {

                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("com.example.restart.RECEIVER"));
        if (connServiceconn != null){
            unbindService(connServiceconn);
        }
    }

}
