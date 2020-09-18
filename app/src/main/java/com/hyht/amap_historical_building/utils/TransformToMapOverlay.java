package com.hyht.amap_historical_building.utils;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.*;
import com.hyht.amap_historical_building.entity.TBasic;

import java.util.ArrayList;
import java.util.List;

public class TransformToMapOverlay {
    private List<TBasic> tBasicList;
    private TBasic tBasic;
    private AMap aMap;

    public TransformToMapOverlay(List<TBasic> tBasicList, AMap aMap, int categories) {
        this.tBasicList = tBasicList;
        this.aMap = aMap;
        transform(categories);
    }

    public TransformToMapOverlay(TBasic tBasic, AMap aMap) {
        this.tBasic = tBasic;
        this.aMap = aMap;
        tBasicList = new ArrayList<>();
        tBasicList.add(tBasic);
        transform(0);
    }

    private void transform(int categories){
        for (TBasic tBasic : tBasicList
        ) {
            List<LatLng> latLngList = new GetCoordinateUtil().getGeoPointList(tBasic);
            if(latLngList.size() == 1 && (categories == 0 || categories == 1)){
                EntityToOverlay entityToOverlay = new EntityToOverlay(aMap, tBasic);
            }
            if ((latLngList.size()>1) && (categories == 0 || categories == 2)){
                EntityToOverlay entityToOverlay = new EntityToOverlay(aMap, tBasic);
            }
        }
    }
}
