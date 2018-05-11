package com.edc.dmt.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import com.edc.dmt.receiver.StartUpReceiver;

public class StartService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
    public void onCreate() {
        super.onCreate();
        StartUpReceiver startUpReceiver = new StartUpReceiver();
        registerReceiver(startUpReceiver, new IntentFilter(Intent.ACTION_BOOT_COMPLETED));
    }

}
