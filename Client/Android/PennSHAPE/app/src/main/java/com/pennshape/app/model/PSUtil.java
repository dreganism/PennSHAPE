package com.pennshape.app.model;

import java.util.Calendar;

/**
 * Created by hasun on 10/18/15.
 */
public class PSUtil {
    public static boolean sameCalender(Calendar a, Calendar b){
        return Math.abs(a.getTime().getTime()-b.getTime().getTime())<=1;
    }

    public static boolean beforeCalender(Calendar a, Calendar b){
        return b.getTime().getTime()-a.getTime().getTime() > -1;
    }

    public static String[] weekDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
}
