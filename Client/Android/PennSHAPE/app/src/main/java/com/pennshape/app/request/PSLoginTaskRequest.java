package com.pennshape.app.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hasun on 11/5/15.
 */
public class PSLoginTaskRequest extends PSHttpTaskRequest {
    private String userName = "";
    private String group = "";
    @Override
    protected String getUrl() {
        return getBaseURL()+"users/" + userName;
    }

    @Override
    protected Object parseResult(JSONObject jsonObject) {
        String userID = null;
        try {
            JSONObject userInfo = jsonObject.getJSONObject("202");
            group = userInfo.getString("groupid");
            userID = userInfo.getString("uid");
        } catch (JSONException e) {
            error = "Login task failing";
        }
        return userID;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getGroup() {
        return group;
    }
}
