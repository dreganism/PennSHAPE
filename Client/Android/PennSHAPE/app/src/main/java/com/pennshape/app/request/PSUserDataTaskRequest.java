package com.pennshape.app.request;

import com.pennshape.app.model.PSDataStore;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hasun on 11/4/15.
 */
public class PSUserDataTaskRequest extends PSHttpTaskRequest {
    private String userID = "";
    @Override
    protected String getUrl() {
        return getBaseURL()+"join/" + userID;
    }

    @Override
    protected Object parseResult(JSONObject jsonObject) {
        JSONObject userData = null;
        try {
            userData = jsonObject.getJSONObject("202");
        } catch (JSONException e) {
            error = "User data task failing";
        }
        return userData;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }
}
