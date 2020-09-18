package com.hyht.amap_historical_building.utils;

import com.amap.api.maps.model.LatLng;
import com.hyht.amap_historical_building.entity.TBasic;

import java.util.ArrayList;
import java.util.List;

public class GetCoordinateUtil {

    public List<LatLng> getGeoPointList(TBasic tBasic){
        List<LatLng> points = new ArrayList<>();
        String addition = tBasic.getPositionCoordinates();
        if (addition != null && addition != "") {
            List<String> additionList = new ArrayList<>();
            String[] strArr = addition.split("/");
            for (String s : strArr
            ) {
                additionList.add(s);
            }

            for (String s : additionList
            ) {
                String[] pointAddition = s.split(",");
                LatLng point = new LatLng(Double.valueOf(pointAddition[0]), Double.valueOf(pointAddition[1]));
                points.add(point);
            }
        }
        return points;
    }
}
