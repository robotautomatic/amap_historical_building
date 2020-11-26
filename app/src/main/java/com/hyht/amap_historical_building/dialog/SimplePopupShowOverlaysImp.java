package com.hyht.amap_historical_building.dialog;

import android.content.Context;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.*;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyht.amap_historical_building.EntService;
import com.hyht.amap_historical_building.entity.TBasic;
import com.hyht.amap_historical_building.listener.OnInFoWindowClickListenerShowDetail;
import com.hyht.amap_historical_building.utils.EntityToOverlay;
import com.hyht.amap_historical_building.utils.GetCoordinateUtil;
import com.xuexiang.xui.widget.toast.XToast;

import java.lang.reflect.Type;
import java.util.List;

public class SimplePopupShowOverlaysImp {
    private Context context;
    private AMap aMap;

    public SimplePopupShowOverlaysImp(Context context, AMap aMap, int categories ) {
        this.context = context;
        this.aMap = aMap;
        select(categories);
    }

    void select(int categories){
        if (categories == 3){
            return;
        }
        EntService entService = new EntService(context);
        entService.buildSelect(new EntService.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                final Type type = new TypeToken<List<TBasic>>() {
                }.getType();
                final Gson gson = new Gson();
                final List<TBasic> basicList = gson.fromJson(result, type);

                EntityToOverlay entityToOverlay;
                for (TBasic tBasic : basicList
                ) {
                    List<LatLng> latLngList = new GetCoordinateUtil().getGeoPointList(tBasic);
                    if(latLngList.size() == 1 && (categories == 0 || categories == 1)){
                        entityToOverlay = new EntityToOverlay(aMap, tBasic, context);
                        entityToOverlay.transform();
                    }
                    if ((latLngList.size()>1) && (categories == 0 || categories == 2)){
                        entityToOverlay = new EntityToOverlay(aMap, tBasic, context);
                        entityToOverlay.transform();
                    }
                }

                aMap.setOnInfoWindowClickListener(new OnInFoWindowClickListenerShowDetail(context, aMap));
            }

            @Override
            public void onError(VolleyError error) {
                XToast.normal(context, "错误，无法获取建筑物信息" + error);
            }
        });
    }
}
