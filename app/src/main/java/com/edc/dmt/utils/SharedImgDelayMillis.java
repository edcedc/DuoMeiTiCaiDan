package com.edc.dmt.utils;

import android.content.Context;

import com.blankj.utilcode.util.StringUtils;


public class SharedImgDelayMillis {

	private static SharedPreferencesTool share;

	private SharedImgDelayMillis() {
	}

	private static SharedImgDelayMillis instance = null;
	private final String MOBILE_KEY = "mobile";

	public static SharedImgDelayMillis getInstance(Context context) {
		if (instance == null) {
			instance = new SharedImgDelayMillis();
		}
		share = SharedPreferencesTool.getInstance(context, "account");
		return instance;
	}

	public void save(long mobile) {
		share.putLong(MOBILE_KEY, mobile);
		share.commit();
	}

	public void delete() {
		share.remove(MOBILE_KEY);
		share.commit();
	}

	public long getDelayMillis() {
		return share.getLong(MOBILE_KEY, 2000);
	}

}
