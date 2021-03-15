package com.learn.lib_commin_ui.base;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.learn.lib_commin_ui.utils.StatusBarUtil;

public class BaseActivity extends FragmentActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("=====================");
        // 沉浸式
        StatusBarUtil.statusBarLightMode(this);
    }
}
