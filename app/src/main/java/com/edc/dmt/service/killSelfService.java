package com.edc.dmt.service;

/**
 * Created by Administrator on 2018/1/26 0026.
 */


import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;

/***
 * 该服务只用来让APP重启，生命周期也仅仅是只是重启APP。重启完即自我杀死
 */
public class killSelfService implements ServiceConnection {


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LogUtils.e("------开启killSelfService服务成功---");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}