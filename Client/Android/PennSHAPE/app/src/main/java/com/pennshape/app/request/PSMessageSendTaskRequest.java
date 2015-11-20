package com.pennshape.app.request;

import com.pennshape.app.model.PSMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by hasun on 11/19/15.
 */
public class PSMessageSendTaskRequest extends PSHttpTaskRequest {
    private String userID = "";
    private String time = "";
    private String msg = "";
    private String groupid = "";
    @Override
    protected String getUrl() {
        return getBaseURL()+"setgroupmessage/" + userID;
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

    public void setTime(String time){
        this.time = time;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    public void setGroupid(String groupid){
        this.groupid = groupid;
    }

    private String getDataString(){
        String dataString = "";
        JSONObject json = new JSONObject();
        try {
            json.put("time", time);
            json.put("message", msg);
            json.put("groupid", groupid);
            dataString = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataString;
    }
}
