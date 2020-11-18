package com.hyht.amap_historical_building;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import androidx.core.app.ActivityCompat;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.activity.BaseSplashActivity;
import com.xuexiang.xutil.app.ActivityUtils;

public class SplashActivity extends BaseSplashActivity {
    @Override
    protected long getSplashDurationMillis() {
        return 2000;
    }

    @Override
    public void onCreateActivity() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initSplashView(R.drawable.bg_splash);
        startSplash(true);

    }

    @Override
    public void onSplashFinished() {
        ActivityUtils.startActivity(MainActivity.class);
        finish();
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }

}
