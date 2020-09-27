package com.hyht.amap_historical_building.utils;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.*;
import com.hyht.amap_historical_building.entity.PolygonBasic;
import com.hyht.amap_historical_building.entity.TBasic;

import java.util.List;

public class EntityToOverlay {
    private AMap aMap;
    private TBasic tBasic;

    public EntityToOverlay(AMap aMap, TBasic tBasic) {
        this.aMap = aMap;
        this.tBasic = tBasic;
    }

    public Marker transform(){
        List<LatLng> latLngList = new GetCoordinateUtil().getGeoPointList(tBasic);
        Marker marker = null;
        if(latLngList.size() == 1){
            marker = aMap.addMarker(new MarkerOptions().position(latLngList.get(0))
                    .title("建筑名称：" + tBasic.getBuildingName())
                    .snippet("建筑编号：" + tBasic.getBuildingNumber()));
            marker.setObject(tBasic);
            return marker;
        }
        if (latLngList.size() > 1){

            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.strokeWidth(5) // 多边形的边框
                    .strokeColor(0xAA000000) // 边框颜色
                    .fillColor(0xAAFF0000);   // 多边形的填充色
            Polygon polygon = aMap.addPolygon(polygonOptions);
            polygon.setPoints(latLngList);
            double longitude = 0.0;
            double latitude = 0.0;
            for (LatLng latlng : latLngList
            ) {
                longitude += latlng.longitude;
                latitude += latlng.latitude;
            }
            LatLng centerPoint = new LatLng(latitude/latLngList.size(), longitude/latLngList.size());
            marker = aMap.addMarker(new MarkerOptions().position(centerPoint)
                    .title("建筑名称：" + tBasic.getBuildingName())
                    .snippet("建筑编号：" + tBasic.getBuildingNumber()));
            PolygonBasic polygonBasic = new PolygonBasic();
            polygonBasic.setPolygon(polygon);
            polygonBasic.setTBasic(tBasic);
            marker.setObject(polygonBasic);
            
            return marker;
        }
        return marker;
    }
}
