package com.hyht.amap_historical_building;

import android.content.Intent;
import android.view.KeyEvent;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.activity.BaseSplashActivity;
import com.xuexiang.xutil.app.ActivityUtils;

/**
 * 渐近式启动页
 *
 * @author xuexiang
 * @since 2018/11/27 下午5:24
 */
public class SplashActivity extends BaseSplashActivity {
    @Override
    protected long getSplashDurationMillis() {
        return 500;
    }

    @Override
    public void onCreateActivity() {
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
