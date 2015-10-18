/**
 * Created by hasun on 10/13/15.
 */

package com.pennshape.app.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PSUser {
    private String uid;
    private String email;
    private String phone;
    private String displayName;
    private String age;
    private String height;
    private String weight;
    private String photo;
    public PSUser (JSONObject json) throws JSONException{
        uid = (String)json.getString("uid");
        email = (String)json.getString("email");
        phone = (String)json.getString("phone");
        displayName = (String)json.getString("displayname");
        age = (String)json.getString("age");
        height = (String)json.getString("height");
        weight = (String)json.getString("weight");
        photo = (String)json.getString("photo");
    }

    public String getID(){ return uid; }
    public String getName(){ return displayName; }
    public Integer getHeight() { return  Integer.parseInt(height); }
    public Integer getWeight() { return Integer.parseInt(weight); }
    public String getPhoto() { return photo; }
    public String getWeightDesc() {
        Integer lb = Math.round(getWeight() * 2.2046f);
        return ""+lb+" lb";
    }
    public String getHeightDesc() {
        Integer ft = (int)Math.floor(getHeight() * 0.03);
        Integer in = (int)Math.floor(getHeight() * 0.39)%12;
        return ft+"'"+in+"''";
    }
    public String getBMIDesc() {
        float bmi = getWeight()*10000.0f/(getHeight()*getHeight());
        return String.format("%.2f", bmi);
    }
    public String getAgeDesc() {
        return new String(age);
    }
}
