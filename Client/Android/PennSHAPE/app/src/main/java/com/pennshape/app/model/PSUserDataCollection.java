/**
 * Created by hasun on 10/14/15.
 */
package com.pennshape.app.model;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class PSUserDataCollection {
    protected String uid;
    protected HashMap<String, PSDailyData> dataCollection;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);

    public PSUserDataCollection(String uid, JSONArray array) throws JSONException{
        this.uid = uid;
        this.dataCollection = new HashMap<String, PSDailyData>();
        for (int i = 0; i < array.length(); i++) {
            PSDailyData dailyData = new PSDailyData(array.getJSONObject(i));
            dataCollection.put(dateDesc(dailyData.date), dailyData);
        }
    }

    public PSDailyData getDailyData(Long date) {
        return dataCollection.get(dateDesc(date));
    }

    public static String dateDesc(Long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return dateFormat.format(cal.getTime());
    }
}
