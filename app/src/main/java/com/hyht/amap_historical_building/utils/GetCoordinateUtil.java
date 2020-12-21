package com.hyht.amap_historical_building.utils;

import com.amap.api.maps.model.LatLng;
import com.hyht.amap_historical_building.entity.TBasic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetCoordinateUtil {

    public List<LatLng> getGeoPointList(TBasic tBasic){
        List<LatLng> points = new ArrayList<>();
        String addition = tBasic.getPositionCoordinates();
        if (addition != null && addition != "" && !addition.isEmpty()) {
            List<String> additionList = new ArrayList<>();
            String[] strArr = addition.split("/");
            for (String s : strArr
            ) {
                additionList.add(s);
            }
            Wgs84CoordinateConverter wgs84CoordinateConverter = new Wgs84CoordinateConverter();
            for (String s : additionList
            ) {
                String[] pointAddition = s.split(",");
                try {
                    if (pointAddition[0].length() == 11){
                        double[] coordinate = wgs84CoordinateConverter.GaussToBL(Double.valueOf(pointAddition[0]), Double.valueOf(pointAddition[1])-500000);
                        LatLng point = new LatLng(coordinate[0],coordinate[1]);
                        points.add(point);
                    }else {
                        LatLng point = new LatLng(Double.valueOf(pointAddition[0]), Double.valueOf(pointAddition[1]));
                        points.add(point);
                    }
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        }
        return points;
    }
}
