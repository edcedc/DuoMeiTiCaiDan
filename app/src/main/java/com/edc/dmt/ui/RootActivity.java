package com.edc.dmt.ui;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.mrwang.stacklibrary.KeyCallBack;
import com.mrwang.stacklibrary.R.id;
import com.mrwang.stacklibrary.RootFragment;
import com.mrwang.stacklibrary.StackManager;

public abstract class RootActivity extends AppCompatActivity {
    public StackManager manager;
    public KeyCallBack callBack;

    public RootActivity() {
    }

    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new LayoutParams(-1, -1));
        frameLayout.setId(id.framLayoutId);
        this.setContentView(frameLayout);
        RootFragment fragment = this.getRootFragment();
        this.manager = new StackManager(this);
        this.manager.setFragment(fragment);
        this.onCreateNow(savedInstanceState);
    }

    @NonNull
    protected abstract RootFragment getRootFragment();

    public void setAnim(@AnimRes int nextIn, @AnimRes int nextOut, @AnimRes int quitIn, @AnimRes int quitOut) {
        this.manager.setAnim(nextIn, nextOut, quitIn, quitOut);
    }

    public void onCreateNow(Bundle savedInstanceState) {
    }

}
