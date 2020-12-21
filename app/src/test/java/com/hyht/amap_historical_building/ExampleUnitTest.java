package com.hyht.amap_historical_building;

import com.hyht.amap_historical_building.utils.*;
import okhttp3.internal.Util;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void GaussToBl() {
        double x = 3229318.104;
        double y = 501388.440;
        Wgs84CoordinateConverter wgs84CoordinateConverter = new Wgs84CoordinateConverter();

        System.out.println("GaussToBL = " + Arrays.toString(wgs84CoordinateConverter.GaussToBL(x, y-500000)));
    }


    @Test
    public void testCoordinate() {
        double x = 3231564.918;
        double y = 505843.495;
        Wgs84CoordinateConverter wgs84CoordinateConverter = new Wgs84CoordinateConverter();

        System.out.println("GaussToBL = " + Arrays.toString(wgs84CoordinateConverter.GaussToBL(x, y)));

        System.out.println("GaussToBL = " + Arrays.toString(wgs84CoordinateConverter.GaussToBL2(x, y)));
        System.out.println("GaussToBL = " + Arrays.toString(GaussXYDeal.GaussToBL(x, y)));
        GaussXYDeal.GaussToBLToGauss(29.131235288, 120.001882597);
        Conversion conversion = new Conversion();

        System.out.println(conversion.UTMXYToLatLon(y, -x));
        System.out.println(conversion.UTMXYToLatLon(-y, x));
        System.out.println(conversion.UTMXYToLatLon(-x, y));
        System.out.println(conversion.UTMXYToLatLon(x, -y));
        System.out.println(conversion.MapXYToLatLon(y, x,0));
        System.out.println(conversion.MapXYToLatLon(x, y,120.00));

        double[] doubles = new double[2];
        GeoConvert.MapXYToLatLon(x,y,119,doubles);
        System.out.println("ddddd = " + Arrays.toString(doubles));

        double[] doubles2 = new double[2];
        GeoConvert.UTMXYToLatLon(x,y,119,false,doubles2);
        System.out.println("2222 = " + Arrays.toString(doubles));
        System.out.println(conversion.UTMXYToLatLon(x, y));
        System.out.println(conversion.UTMXYToLatLon(y, x, 119,false));
        System.out.println("GaussToBL = " + Arrays.toString(GaussXYDeal.GaussToBL(y, x)));
    }
    @Test
    public void testCoordinate2(){
        double x = 29.27621459140641;
        double y = 120.04554543168692;GaussXYDeal.GaussToBLToGauss(x,y);
        Wgs84CoordinateConverter wgs84CoordinateConverter = new Wgs84CoordinateConverter();
        System.out.println("BLToGauss = " + Arrays.toString(wgs84CoordinateConverter.BLToGauss(x,y)));
    }
    @Test
    public void addition_isCorrect() {

        List<Integer> a = new ArrayList();
        a.add(1);
        a.add(2);
        String b="123";
        System.out.println(Integer.parseInt(b)+1);
    }
    @Test
    public void slowestKey() {
        int[] releaseTimes = {9,29,49,50};
        String keysPressed = "cbcd";
        int max = releaseTimes[0];
        List<Integer> maxTime = new ArrayList();
        maxTime.add(0);
        int sub = 0;
        for(int i = 1; i < releaseTimes.length; i++){
            sub = releaseTimes[i] - releaseTimes[i-1];
            if(sub == max){
                maxTime.add(i);
            }
            if(sub > max){
                max = sub;
                maxTime.clear();
                maxTime.add(i);
            }
        }
        System.out.println(maxTime);
        char result = keysPressed.charAt(maxTime.get(0));
        for(int i = 1; i < maxTime.size(); i++){
            if(keysPressed.charAt(maxTime.get(i)) > result){
                result = keysPressed.charAt(maxTime.get(i));
            }
        }
        System.out.println(keysPressed.charAt(2) > keysPressed.charAt(1));
        System.out.println(result);
        List a = new
                ArrayList();
        a.remove(0);
    }
}