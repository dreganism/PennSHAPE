/**
 * Created by hasun on 10/14/15.
 */
package com.pennshape.app.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PSDailyData {
    protected Integer date;
    protected Integer c1 = 0;
    protected Integer c2 = 0;
    protected Integer c3 = 0;
    protected Integer steps = 0;
    protected Integer cal = 0;
    public PSDailyData(JSONObject json) throws JSONException{
        date = Integer.parseInt((String)json.getString("date"));
        c1 = Integer.parseInt((String)json.getString("c1"));
        c2 = Integer.parseInt((String)json.getString("c2"));
        c3 = Integer.parseInt((String)json.getString("c3"));
        steps = Integer.parseInt((String)json.getString("steps"));
        cal = Integer.parseInt((String)json.getString("cal"));
    }

    public Integer getC1(){ return c1; }
    public Integer getC2(){ return c2; }
    public Integer getC3(){ return c3; }
    public Integer getSteps(){ return steps; }
    public Integer getCal(){ return cal; }
    public float getFormula() {
        return c1+c2+c3+steps/100f;
    }

}
