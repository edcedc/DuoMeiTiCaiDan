package com.edc.dmt.mar;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.edc.dmt.BuildConfig;
import com.edc.dmt.MainActivity;
import com.edc.dmt.R;
import com.edc.dmt.ui.RootActivity;
import com.nanchen.crashmanager.CrashApplication;
import com.nanchen.crashmanager.UncaughtExceptionHandlerImpl;
import com.wanjian.cockroach.Cockroach;

public class MyApplication extends CrashApplication {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        // 设置崩溃后自动重启 APP
        UncaughtExceptionHandlerImpl.getInstance().init(this, BuildConfig.DEBUG, true, 0, MainActivity.class);

        // 禁止重启
//        UncaughtExceptionHandlerImpl.getInstance().init(this,BuildConfig.DEBUG);
//        initCrash();
//        LogUtils.getConfig().setLogSwitch(false);
    }

    private void initCrash() {
        final String catalogPath = Environment.getExternalStorageDirectory() + "/" + this.getResources().getString(R.string.app_name);

        Cockroach.install(new Cockroach.ExceptionHandler() {
            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException
            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                //开发时使用Cockroach可能不容易发现bug，所以建议开发阶段在handlerException中用Toast谈个提示框，
                //由于handlerException可能运行在非ui线程中，Toast又需要在主线程，所以new了一个new Handler(Looper.getMainLooper())，
                //所以千万不要在下面的run方法中执行耗时操作，因为run已经运行在了ui线程中。
                //new Handler(Looper.getMainLooper())只是为了能弹出个toast，并无其他用途
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //建议使用下面方式在控制台打印异常，这样就可以在Error级别看到红色log
                            if (FileUtils.createOrExistsDir(catalogPath)) {
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                CrashUtils.init(catalogPath, new CrashUtils.OnCrashListener() {
                                    @Override
                                    public void onCrash(String crashInfo, Throwable e) {
                                        LogUtils.e("AndroidRuntime", "--->CockroachException:" + thread + "<---", e);

                                    }
                                });
                            }
                        } catch (Throwable e) {
                            LogUtils.e(e.getMessage());
                        }
                    }
                });
            }
        });
    }

}
