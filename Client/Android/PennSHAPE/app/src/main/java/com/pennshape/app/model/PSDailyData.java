/**
 * Created by hasun on 10/14/15.
 */
package com.pennshape.app.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class PSDailyData {
    protected Long date;
    protected Integer c1 = 0;
    protected Integer c2 = 0;
    protected Integer c3 = 0;
    protected String d1 = "******";
    protected String d2= "******";
    protected String d3 = "******";
    protected Integer steps = 0;
    protected Integer cal = 0;
    public PSDailyData(JSONObject json) throws JSONException{
        date = json.getLong("date");
        parseC1((String)json.getString("c1"));
        parseC2((String)json.getString("c2"));
        parseC3((String)json.getString("c3"));
        steps = json.getInt("steps");
        cal = json.getInt("cal");
    }

    public Integer getC1(){ return c1; }
    public Integer getC2(){ return c2; }
    public Integer getC3(){ return c3; }
    public String getD1(){ return d1; }
    public String getD2(){ return d2; }
    public String getD3(){ return d3; }
    public Integer getSteps(){ return steps; }
    public Integer getCal(){ return cal; }
    public float getFormula() {
        return c1+c2+c3+steps/100f;
    }

    private void parseC1(String data){
        if(data.length()==0) return;
        String[] subStrings  = data.split("#");
        if(subStrings.length==2){
            c1 = Integer.parseInt(subStrings[0]);
            d1 = subStrings[1];
        }
    }

    private void parseC2(String data){
        if(data.length()==0) return;
        String[] subStrings  = data.split("#");
        if(subStrings.length==2){
            c2 = Integer.parseInt(subStrings[0]);
            d2 = subStrings[1];
        }
    }

    private void parseC3(String data){
        if(data.length()==0) return;
        String[] subStrings  = data.split("#");
        if(subStrings.length==2){
            c3 = Integer.parseInt(subStrings[0]);
            d3 = subStrings[1];
        }
    }

}
