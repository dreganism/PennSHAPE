package com.pennshape.app.request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by hasun on 11/5/15.
 */
public class PSDataUploadTaskRequest extends PSHttpTaskRequest {
    private String userID = "";
    private String dataString = "";

    @Override
    protected String getUrl() {
        return getBaseURL()+"activities/" + userID;
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
    protected void setupConnection(HttpURLConnection urlConnection) throws IOException{
        urlConnection.setDoOutput(true);
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
        os.writeBytes(dataString);
        os.flush();
        os.close();
    }

    public void setUserID(String userID){
        this.userID = userID;
    }
    public void setDataString(String data){
        this.dataString = data;
    }
}
