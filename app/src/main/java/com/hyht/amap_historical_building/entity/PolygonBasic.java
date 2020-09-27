package com.hyht.amap_historical_building.entity;

import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polygon;
import lombok.Data;

@Data
public class PolygonBasic {
    private Polygon polygon;
    private TBasic tBasic;
}
