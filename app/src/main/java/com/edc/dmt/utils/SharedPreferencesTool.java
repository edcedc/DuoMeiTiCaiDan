package com.edc.dmt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;
import java.util.Set;

/**
 * SharedPreferences和Editor的代理类，把这两个接口统一放一起，简化代码
 * @author 
 * @time:2012-11-30
 */
public class SharedPreferencesTool implements SharedPreferences,Editor {
	
	private SharedPreferences mSharedPreferences;
	private Editor mEditor;
	
	private SharedPreferencesTool(Context context, String name){
		mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		mEditor = edit();
	}
	
	public static SharedPreferencesTool getInstance(Context context, String name){
		return new SharedPreferencesTool(context,name);
	}
	
	@Override
	public Map<String, ?> getAll() {
		return mSharedPreferences.getAll();
	}

	@Override
	public String getString(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}

	@Override
	public int getInt(String key, int defValue) {
		return mSharedPreferences.getInt(key, defValue);
	}

	@Override
	public long getLong(String key, long defValue) {
		return mSharedPreferences.getLong(key, defValue);
	}

	@Override
	public float getFloat(String key, float defValue) {
		return mSharedPreferences.getFloat(key, defValue);
	}

	@Override
	public boolean getBoolean(String key, boolean defValue) {
		return mSharedPreferences.getBoolean(key, defValue);
	}

	@Override
	public boolean contains(String key) {
		return mSharedPreferences.contains(key);
	}

	/**
	 * 获取SharedPreferences一个可编辑对象实例
	 */
	@Override
	public Editor edit() {
		return mSharedPreferences.edit();
	}

	@Override
	public void registerOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
	}

	@Override
	public void unregisterOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
	}

	@Override
	public Editor putString(String key, String value) {
		return mEditor.putString(key, value);
	}

	@Override
	public Editor putInt(String key, int value) {
		return mEditor.putInt(key, value);
	}

	@Override
	public Editor putLong(String key, long value) {
		return mEditor.putLong(key, value);
	}

	@Override
	public Editor putFloat(String key, float value) {
		return mEditor.putFloat(key, value);
	}

	@Override
	public Editor putBoolean(String key, boolean value) {
		return mEditor.putBoolean(key, value);
	}
	
	/**
	 * 删除指定key的内容
	 */
	@Override
	public Editor remove(String key) {
		return mEditor.remove(key);
	}

	/**
	 * 清空所有内容
	 */
	@Override
	public Editor clear() {
		return mEditor.clear();
	}

	/**
	 * 提交数据
	 */
	@Override
	public boolean commit() {
		return mEditor.commit();
	}

	@Override
	public void apply() {
		
	}

	@Override
	public Editor putStringSet(String arg0, Set<String> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getStringSet(String arg0, Set<String> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
