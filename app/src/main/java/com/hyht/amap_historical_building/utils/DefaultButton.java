package com.hyht.amap_historical_building.utils;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import com.hyht.amap_historical_building.R;
import com.xuexiang.xui.widget.layout.XUIButton;

import static com.xuexiang.xui.utils.ResUtils.getDrawable;
/**
 * 封装的默认按钮样式
 */
public class DefaultButton {
    Context context;
    Button btn_default;
    public DefaultButton(Context context) {
        this.context = context;
        btn_default = new XUIButton(context);
        btn_default.setBackground(getDrawable(R.drawable.rb_bg_selector));
        btn_default.setTextAppearance(R.style.Button_Radius);
        btn_default.setLayoutParams(new LinearLayout.LayoutParams(btn_default.getLayoutParams().WRAP_CONTENT,btn_default.getLayoutParams().WRAP_CONTENT));
        LinearLayout.LayoutParams  layoutParams = (LinearLayout.LayoutParams) btn_default.getLayoutParams();
        layoutParams.setMargins(20,20,20,20);
        btn_default.setLayoutParams(layoutParams);
        btn_default.setPaddingRelative(12,0,12,0);
    }

    public Button getDefaultButton() {
        return btn_default;
    }
}
