package com.hyht.amap_historical_building;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.just.agentweb.AgentWeb;

/**
 * 自定义web浏览器
 */
public class WebActivity extends AppCompatActivity {
    AgentWeb mAgentWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(null, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}
