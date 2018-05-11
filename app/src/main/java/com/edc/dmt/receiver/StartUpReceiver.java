package com.edc.dmt.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.blankj.utilcode.util.LogUtils;
import com.edc.dmt.MainActivity;

public class StartUpReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {
//			LogUtils.e("倒计时开始");
			SystemClock.sleep(3*1000);
			
			Intent intent1 = new Intent(context, MainActivity.class);
			intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent1);
			
		}
	}

}
