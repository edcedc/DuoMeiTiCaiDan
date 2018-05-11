package com.edc.dmt.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.edc.dmt.FileComparator;
import com.edc.dmt.base.BaseFragment;
import com.edc.dmt.R;
import com.edc.dmt.databinding.ActivityMainBinding;
import com.edc.dmt.service.RestartAppService;
import com.edc.dmt.service.killSelfService;
import com.edc.dmt.utils.SharedImgDelayMillis;
import com.edc.dmt.video.EmptyControlVideo;
import com.edc.dmt.view.BottomBar;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.wanjian.cockroach.Cockroach;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by yc on 2018/2/27.
 * 首页
 */

public class HomeFrg extends BaseFragment<ActivityMainBinding> implements VideoAllCallBack, View.OnClickListener, TextWatcher {

    private int fileNameItem = 0;
    private long mDelayMillis = 2000;//默认图片显示秒数
    private List<GSYVideoModel> listMp4 = new ArrayList<>();
    private List<String> listImg = new ArrayList<>();
    private List<String> listAll = new ArrayList<>();

    private final int mHandler_mp4 = 0;
    private final int mHandler_img = 1;
    private final int mHandler_logo = 2;

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initParms(Bundle bundle) {
    }

    @Override
    protected void initView(View view) {
        mB.etMin.addTextChangedListener(this);
        mB.btConfirm.setOnClickListener(this);
        mB.fyLayout.setOnClickListener(this);
        mB.videoPlayer.setIfCurrentIsFullscreen(true);
        mB.videoPlayer.setVideoAllCallBack(this);

        mDelayMillis = SharedImgDelayMillis.getInstance(act).getDelayMillis();
        mB.etMin.setText((int) (SharedImgDelayMillis.getInstance(act).getDelayMillis() / 1000) + "");

        final String appName = act.getResources().getString(R.string.app_file);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String dirPath = Environment.getExternalStorageDirectory() + "/" + appName;
                if (FileUtils.createOrExistsDir(dirPath)) {
                    List<File> files = FileUtils.listFilesInDir(dirPath);
                    Collections.sort(files, new FileComparator());
                    if (files != null && files.size() != 0) {
                        for (int i = 0; i < files.size(); i++) {
                            final File file = files.get(i);
                            if (file.toString().endsWith(".mp4")) {
                                listMp4.add(new GSYVideoModel(file.toString(), ""));
                            } else {
                                if (!StringUtils.isEmpty(ImageUtils.getImageType(file.toString()))) {
                                    listImg.add(file.toString());
                                } else {
                                    showToast(mB.tvText, "文件类型有错");
                                    return;
                                }
                            }
                            listAll.add(file.toString());

                        }
                        setPlayDmt();
                    } else {
                        mHandler.sendEmptyMessage(mHandler_logo);
                        showToast( "找不到" + appName + "文件夹或者文件夹没有文件");
                    }
                } else {
                    mHandler.sendEmptyMessage(mHandler_logo);
                    showToast("文件夹创建失败，请查看是否权限被关闭");
                }
            }
        }).start();
    }

    /**
     *  播放开始操作
     */
    private void setPlayDmt() {
//        mTv_text.setText("");
        fileNameItem = 0;
        if (listAll.get(fileNameItem).indexOf(".mp4") != -1){
            mHandler.sendEmptyMessage(mHandler_mp4);
        }else {
            mHandler.sendEmptyMessage(mHandler_img);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mB.videoPlayer.onVideoPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mB.videoPlayer.onVideoResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mB.videoPlayer.release();
        mB.videoPlayer.clearCurrentCache();
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            LogUtils.e(listAll.get(fileNameItem));
            switch (msg.what) {
                case mHandler_mp4:
                    setViewVisibility(mB.videoPlayer);
                    List<GSYVideoModel> bean = new ArrayList<>();
                    bean.add(new GSYVideoModel(listAll.get(fileNameItem), ""));
                    mB.videoPlayer.setUp(bean, false, 0);
                    mB.videoPlayer.startPlayLogic();
                    if (listAll.size() == 1){
                        mB.videoPlayer.setLooping(true);
                    }
                    break;
                case mHandler_img:
                    setViewVisibility(mB.ivImg);
                    Glide.with(act).load(listAll.get(fileNameItem)).dontAnimate().placeholder(mB.ivImg.getDrawable()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            mHandler.sendEmptyMessage(mHandler_logo);
                            showToast("图片加载失败");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if (listAll.size() != 1){
                                new Handler().postDelayed(new Runnable(){
                                    public void run() {
                                        if ((listAll.size() - 1 == fileNameItem) && listAll.size() != 1){
                                            setPlayDmt();
                                            return;
                                        }else {
                                            mHandler.sendEmptyMessage(getNextFileName() == true ? 0 : 1);
                                        }

                                    }
                                }, mDelayMillis);
                            }
                            return false;
                        }
                    }).into(mB.ivImg);
                    break;
                case mHandler_logo:
                    if (mB.ivImg.getVisibility() == View.GONE){
                        mB.ivImg.setVisibility(View.VISIBLE);
                    }
                    Glide.with(act).load(R.mipmap.include_01).into(mB.ivImg);
                    break;
            }
        }
    };

    /**
     *  获取下一个文件名
     */
    private boolean getNextFileName(){
        boolean isName;
        setAddFileNameItem();
        String s = listAll.get(fileNameItem);
        if (s.indexOf(".mp4") != -1){
            isName = true;
        }else {
            isName = false;
        }
        return isName;
    }

    /**
     *  索引值增加
     */
    private void setAddFileNameItem(){
        ++fileNameItem;
    }

    private void setViewVisibility(View view) {
        if (view instanceof GSYVideoPlayer) {
            mB.videoPlayer.setVisibility(View.VISIBLE);
            mB.ivImg.setVisibility(View.GONE);
            mB.ivImg.setImageDrawable(null);
        }else if (view instanceof ImageView){
            mB.videoPlayer.setVisibility(View.GONE);
            mB.ivImg.setVisibility(View.VISIBLE);
        }
    }

    /**
     *  准备开始
     * @param url
     * @param objects
     */
    @Override
    public void onStartPrepared(String url, Object... objects) {
//        LogUtils.e("onStartPrepared");
    }

    /**
     *  准备
     * @param url
     * @param objects
     */
    @Override
    public void onPrepared(String url, Object... objects) {
//        LogUtils.e("onPrepared");
    }

    @Override
    public void onClickStartIcon(String url, Object... objects) {
        LogUtils.e("onClickStartIcon");
    }

    @Override
    public void onClickStartError(String url, Object... objects) {
        LogUtils.e("onClickStartError");
    }

    @Override
    public void onClickStop(String url, Object... objects) {
        LogUtils.e("onClickStop");
    }

    @Override
    public void onClickStopFullscreen(String url, Object... objects) {
        LogUtils.e("onClickStopFullscreen");
    }

    @Override
    public void onClickResume(String url, Object... objects) {
        LogUtils.e("onClickResume");
    }

    @Override
    public void onClickResumeFullscreen(String url, Object... objects) {
        LogUtils.e("onClickResumeFullscreen");
    }

    @Override
    public void onClickSeekbar(String url, Object... objects) {
        LogUtils.e("onClickSeekbar");
    }

    @Override
    public void onClickSeekbarFullscreen(String url, Object... objects) {
        LogUtils.e("onClickSeekbarFullscreen");
    }

    /**
     * 全部播放结束
     */
    @Override
    public void onAutoComplete(String url, Object... objects) {
//        LogUtils.e("onAutoComplete");
        if (listAll.size() - 1 == fileNameItem){
            setPlayDmt();
            return;
        }

        setAddFileNameItem();
        if (listAll.get(fileNameItem).indexOf(".mp4") != -1){
            mHandler.sendEmptyMessage(mHandler_mp4);
        }else {
            mHandler.sendEmptyMessage(mHandler_img);
        }
    }

    @Override
    public void onEnterFullscreen(String url, Object... objects) {
        LogUtils.e("onEnterFullscreen");
    }

    @Override
    public void onQuitFullscreen(String url, Object... objects) {
        LogUtils.e("onQuitFullscreen");
    }

    @Override
    public void onQuitSmallWidget(String url, Object... objects) {
        LogUtils.e("onQuitSmallWidget");
    }

    @Override
    public void onEnterSmallWidget(String url, Object... objects) {
        LogUtils.e("onEnterSmallWidget");
    }

    @Override
    public void onTouchScreenSeekVolume(String url, Object... objects) {
        LogUtils.e("onTouchScreenSeekVolume");
    }

    @Override
    public void onTouchScreenSeekPosition(String url, Object... objects) {
        LogUtils.e("onTouchScreenSeekPosition");
    }

    @Override
    public void onTouchScreenSeekLight(String url, Object... objects) {
        LogUtils.e("onTouchScreenSeekLight");
    }

    @Override
    public void onPlayError(String url, Object... objects) {
        LogUtils.e("onPlayError");
        showToast("播放失败，请重启");
        mHandler.sendEmptyMessage(mHandler_logo);
    }

    /**
     *  点击开始
     * @param url
     * @param objects
     */
    @Override
    public void onClickStartThumb(String url, Object... objects) {
//        LogUtils.e("onClickStartThumb");
    }

    @Override
    public void onClickBlank(String url, Object... objects) {
        LogUtils.e("onClickBlank");
    }

    @Override
    public void onClickBlankFullscreen(String url, Object... objects) {
        LogUtils.e("onClickBlankFullscreen");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirm:
                String str = mB.etMin.getText().toString().trim();
                if (StringUtils.isEmpty(str)) {
                    showToast("输入不能为空");
                    return;
                }
                if (Integer.valueOf(str) > 99){
                    showToast("最多99秒");
                    return;
                }
                StringBuffer sb = new StringBuffer();
                sb.append(str).append("000");
                this.mDelayMillis = Integer.valueOf(sb.toString());
                showToast("设置成功");
                SharedImgDelayMillis.getInstance(act).save(mDelayMillis);
                break;
            case R.id.fy_layout:
                mFinsh++;
                exitBy2Click();
                break;
        }
    }


    private int mFinsh;
    private void exitBy2Click() {
        LogUtils.e(mFinsh);
        Handler tExit = null;
        if (mFinsh < 4) {
//            showToast("再按一次退出程序");
            tExit = new Handler();
            tExit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFinsh = 0; // 取消退出
                }
            }, 3000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
            return;
        } else {
//            Cockroach.uninstall();
//            LogUtils.e("退出成功");
            act.finish();
            System.exit(0);
        }
    }

    /****************EditText监听文字事件********************/
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().equals("0")) {
            showToast("最低一秒");
            return;
        }
        if (s.toString().length() > 2) {
            showToast("最多99秒");
            return;
        }
    }
}
