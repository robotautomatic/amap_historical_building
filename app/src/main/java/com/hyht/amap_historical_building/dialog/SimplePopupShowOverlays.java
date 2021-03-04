package com.hyht.amap_historical_building.dialog;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import com.amap.api.maps.AMap;
import com.hyht.amap_historical_building.R;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup;

import static com.xuexiang.xui.XUI.getContext;

/**
 * 选择建筑物类型简易弹窗
 */
public class SimplePopupShowOverlays {
    XUISimplePopup mListPopup;
    Context context;
    AMap aMap;

    public SimplePopupShowOverlays(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
    }

    private void GenerateSimplePopupShow(Context context, AMap aMap) {
        mListPopup = new XUISimplePopup(context, new String[]{
                "全部显示", "点状建筑", "建筑范围", "取消显示",})
                .create(DensityUtils.dp2px(getContext(), 170), new XUISimplePopup.OnPopupItemClickListener() {
                    @Override
                    public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                        aMap.clear(true);
                        aMap.setOnMarkerClickListener(null);
                        aMap.setOnInfoWindowClickListener(null);
                        aMap.setOnMapClickListener(null);
                        Activity activity = (Activity) context;
                        LinearLayout linearLayout = activity.findViewById(R.id.linear_bt);
                        while (linearLayout.getChildCount() > 6) {
                            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
                        }
                        new SimplePopupShowOverlaysImp(context, aMap, position);
                    }
                })
                .setHasDivider(true);
    }
    public XUISimplePopup GetSimplePopup(){
        GenerateSimplePopupShow(context, aMap);
        return mListPopup;
    }
}
