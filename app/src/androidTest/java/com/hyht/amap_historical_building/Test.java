package com.hyht.amap_historical_building;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.hyht.amap_historical_building.utils.Wgs84CoordinateConverter;

import java.util.Arrays;

public class Test {
    @org.junit.Test
    public void GaussToBl() {
        double x = 3229318.104;
        double y = 501388.440;
        Wgs84CoordinateConverter wgs84CoordinateConverter = new Wgs84CoordinateConverter();
        double[] doubles = wgs84CoordinateConverter.GaussToBL(x, y);
        System.out.println("GaussToBL = " + Arrays.toString(wgs84CoordinateConverter.GaussToBL(x, y)));


        CoordinateConverter converter  = new CoordinateConverter(InstrumentationRegistry.getTargetContext());
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(new LatLng(doubles[0],doubles[1]));
        System.out.println(converter.convert());
    }
}
