package com.hyht.amap_historical_building;

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
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        String a ="qwe";
        String b = "ase";
        System.out.println(a.charAt(2) != b.charAt(2));
        Map<Map<String,String>, Boolean> map = new HashMap<>();
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