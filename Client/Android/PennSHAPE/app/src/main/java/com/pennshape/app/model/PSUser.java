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
    private Integer age;
    private Float height;
    private Float weight;
    private String photo;
    private String favorite;
    public PSUser (JSONObject json) throws JSONException{
        uid = json.getString("uid");
        email = json.getString("email");
        phone = json.getString("phone");
        displayName = json.getString("displayname");
        age = Integer.parseInt(json.getString("age"));
        height = Float.parseFloat(json.getString("height"));
        weight = Float.parseFloat(json.getString("weight"));
        photo = json.getString("pic");
        if(json.has("favorite")){
            favorite = json.getString("favorite");
        }else{
            favorite = "";
        }
    }

    public String getID(){ return uid; }
    public String getName(){ return displayName; }
    public Float getHeight() { return height; }
    public Float getWeight() { return weight; }
    public String getPhoto() { return photo; }
    public String getWeightDesc() {
        Integer lb = Math.round(getWeight());
        return ""+lb+" lb";
    }
    public String getHeightDesc() {
        Integer ft = (int)(getHeight()/12);
        Integer in = (int)(getHeight()%12);
        return ft+"'"+in+"''";
    }
    public String getBMIDesc() {
        float bmi = getWeight()*703/(getHeight()*getHeight());
        return String.format("%.2f", bmi);
    }
    public String getAgeDesc() {
        return ""+age;
    }
    public String getFavorite() {
        return favorite;
    }
}
