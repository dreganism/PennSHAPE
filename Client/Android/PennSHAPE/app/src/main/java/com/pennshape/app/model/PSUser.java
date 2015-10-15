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
    private String firstName;
    private String lastName;
    private String age;
    private String height;
    private String weight;
    private String photo;
    public PSUser (JSONObject json) throws JSONException{
        uid = (String)json.getString("uid");
        email = (String)json.getString("email");
        phone = (String)json.getString("phone");
        firstName = (String)json.getString("first_name");
        lastName = (String)json.getString("last_name");
        age = (String)json.getString("age");
        height = (String)json.getString("height");
        weight = (String)json.getString("weight");
        photo = (String)json.getString("photo");
    }

    public String getID(){ return uid; }
    public String getFirstName(){ return firstName; }
    public String getLastName(){ return  lastName; }
    public Integer getHeight() { return  Integer.parseInt(height); }
    public Integer getWeight() { return Integer.parseInt(weight); }
    public String getPhoto() { return photo; }
}
