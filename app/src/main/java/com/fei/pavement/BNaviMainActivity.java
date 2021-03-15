package com.fei.pavement;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;

public class BNaviMainActivity extends Activity {
    BikeNavigateHelper mNaviHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mNaviHelper = BikeNavigateHelper.getInstance();
// 获取诱导页面地图展示View
//        View view = mNaviHelper.onCreate(BNaviGuideActivity.this);
//
//        if (view != null) {
//            setContentView(view);
//        }

// 开始导航
//        mNaviHelper.startBikeNavi(BNaviGuideActivity.this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mNaviHelper.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNaviHelper.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNaviHelper.quit();
    }
}
