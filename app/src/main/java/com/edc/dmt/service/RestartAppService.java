package com.edc.dmt.service;

/**
 * Created by Administrator on 2018/1/26 0026.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mk_who on 2017/8/3/0003.
 */
public class RestartAppService extends Service {

    private IBinder mBinder=new Binder();
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Intent intent = new Intent("com.example.restart.RECEIVER");

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String dateStr = sdf.format(new Date());
//                        LogUtils.e(dateStr);
//                        ToastUtils.showShort(dateStr);
                        if (dateStr.equals("03:00:00")){
                            sendBroadcast(intent);//发送广播
                        }

                    }
                }).start();
            }
        };
        mTimer.schedule(mTimerTask,1000,1000);
        mTimer=null;
        mTimerTask=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null){
            mTimer.cancel();
            mTimerTask.cancel();
        }
    }

}
