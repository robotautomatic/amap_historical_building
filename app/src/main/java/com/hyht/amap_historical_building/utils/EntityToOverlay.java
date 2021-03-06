package com.hyht.amap_historical_building.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.*;
import com.hyht.amap_historical_building.MainActivity;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.entity.PolygonBasic;
import com.hyht.amap_historical_building.entity.TBasic;

import java.util.List;
/**
 * 将实体类转化成地图上的覆盖物
 */
public class EntityToOverlay {
    private AMap aMap;
    private TBasic tBasic;
    private Context context;

    public EntityToOverlay(AMap aMap, TBasic tBasic, Context context) {
        this.aMap = aMap;
        this.tBasic = tBasic;
        this.context = context;
    }

    public Marker transform(){
        List<LatLng> latLngList = new GetCoordinateUtil().getGeoPointList(tBasic);
        Marker marker;
        View view = View.inflate(context, R.layout.custom_marker, null);
        TextView textViewCustomMarker = view.findViewById(R.id.tv_custom_marker);
        if(latLngList.size() == 1){
            textViewCustomMarker.setText(tBasic.getBuildingName());
            marker = aMap.addMarker(new MarkerOptions().position(latLngList.get(0))
                    .title("建筑名称：" + tBasic.getBuildingName())
                    .snippet("建筑编号：" + tBasic.getBuildingNumber())
                    .anchor(0.5f,0.7f)
                    .icon(BitmapDescriptorFactory.fromView(view)));
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
            textViewCustomMarker.setText(tBasic.getBuildingName());
            marker = aMap.addMarker(new MarkerOptions().position(centerPoint)
                    .title("建筑名称：" + tBasic.getBuildingName())
                    .snippet("建筑编号：" + tBasic.getBuildingNumber())
                    .anchor(0.5f,0.7f)
                    .icon(BitmapDescriptorFactory.fromView(view)));
            PolygonBasic polygonBasic = new PolygonBasic();
            polygonBasic.setPolygon(polygon);
            polygonBasic.setTBasic(tBasic);
            marker.setObject(polygonBasic);
            
            return marker;
        }
        return null;
    }
}
