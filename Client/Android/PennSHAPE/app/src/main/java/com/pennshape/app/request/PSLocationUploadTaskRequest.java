package com.pennshape.app.request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by hasun on 11/12/15.
 */
public class PSLocationUploadTaskRequest extends PSHttpTaskRequest{
    private String userID = "";
    private double longitude = 0.0;
    private double latitude = 0.0;
    private String time = "";
    @Override
    protected String getUrl() {
        return getBaseURL()+"geolocation/" + userID;
    }

    @Override
    protected String getRequestMethod() {
        return "POST";
    }

    @Override
    protected Object parseResult(JSONObject jsonObject) {
        String res = null;
        try {
            res = jsonObject.getString("202");
        } catch (JSONException e) {
            error = "Data upload task failing";
        }
        return res;
    }

    @Override
    protected void setupConnection(HttpURLConnection urlConnection) throws IOException {
        urlConnection.setDoOutput(true);
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
        os.writeBytes(getDataString());
        os.flush();
        os.close();
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String getDataString(){
        String dataString = "";
        JSONObject json = new JSONObject();
        try {
            json.put("geotime",time);
            json.put("lat",""+latitude);
            json.put("lon",""+longitude);
            dataString = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataString;
    }
}
