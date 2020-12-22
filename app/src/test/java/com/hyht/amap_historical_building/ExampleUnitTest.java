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

        System.out.println(PositionUtil.gps84_To_Gcj02(29.180884082634584, 120.01427426098363));
        System.out.println(PositionUtil.gps84_To_Gcj02(29.180870506137452, 120.01427425238207));
        //29.178128210,120.01876284/

        //[29.18088408179141, 120.01427426098357] [29.180884082634584, 120.01427426098363]
        //(29.17817449193069,120.0189040039506)
        //(29.178174492774506,120.01890400395071)
        //(29.178160905890778,120.01890399422963)
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