/**
 * Created by hasun on 10/14/15.
 */
package com.pennshape.app.model;

import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;


public class PSUserDataCollection {
    protected String uid;
    protected SparseArray<PSDailyData> dataCollection;

    public PSUserDataCollection(String uid, JSONArray array) throws JSONException{
        this.uid = uid;
        this.dataCollection = new SparseArray<PSDailyData>();
        for (int i = 0; i < array.length(); i++) {
            PSDailyData dailyData = new PSDailyData(array.getJSONObject(i));
            dataCollection.put(timeInDays(dailyData.date), dailyData);
        }
    }

    public PSDailyData getDailyDataByTimeInSeconds(Integer timeInSecs) {
        return dataCollection.get(timeInDays(timeInSecs));
    }

    public PSDailyData getDailyDataByTimeInDays(Integer timeInDays) {
        return dataCollection.get(timeInDays);
    }

    public static Integer timeInDays(Integer date) {
        return date / 86400;
    }
}
