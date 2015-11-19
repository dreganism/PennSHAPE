package com.pennshape.app.request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hasun on 11/17/15.
 */
public class PSMessagePullTaskRequest extends PSHttpTaskRequest {
    private String groupID = "";
    private String from = "";
    @Override
    protected String getUrl() {
        String url = getBaseURL()+"getgroupmessage/" + groupID;
        if(from.length()>0){
            url += "?from=";
            url +=  from;
        }
        return url;
    }

    @Override
    protected Object parseResult(JSONObject jsonObject) {
        JSONArray messagesArray = null;
        try {
            if(jsonObject.has("groupmessages")){
                messagesArray = jsonObject.getJSONArray("groupmessages");
            }else if(jsonObject.has("503")){
                messagesArray = new JSONArray();
            }
        } catch (JSONException e) {
            error = "Incorrect group message contents";
        }
        return messagesArray;
    }

    @Override
    protected boolean validResult(JSONObject jsonObject) {
        if(jsonObject.has("groupmessages")) return true;
        if(jsonObject.has("503")) return true;
        return false;
    }

    public void setGroupID(String groupID){
        this.groupID = groupID;
    }
    public void setFrom(String from) {
        this.from = from;
    }
}
