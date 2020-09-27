package com.hyht.amap_historical_building.listener;

import android.content.Context;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.hyht.amap_historical_building.dialog.DialogOverlayDetail;
import com.hyht.amap_historical_building.entity.TBasic;

public class OnInFoWindowClickListenerShowDetail implements AMap.OnInfoWindowClickListener {
    private Context context;
    private AMap aMap;

    public OnInFoWindowClickListenerShowDetail(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        DialogOverlayDetail dialogOverlayDetail = new DialogOverlayDetail(context, aMap, marker);
    }
}
