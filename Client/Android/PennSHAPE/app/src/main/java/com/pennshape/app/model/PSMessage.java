package com.pennshape.app.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hasun on 11/18/15.
 */
public class PSMessage {
    protected Integer id;
    protected String uid;
    protected Long time;
    protected Integer groupid;
    protected String msg;
    protected String extra;

    public PSMessage(Integer id, String uid, Integer groupid, String msg, Long time){
        this.id = id;
        this.uid = uid;
        this.groupid = groupid;
        this.msg = msg;
        this.time = time;
    }

    public String getMessage() {
        String escaped = msg.replace("\\n","\n");
        return escaped;
    }

    public String getUid() {
        return uid;
    }

    public Long getTime() {
        return time;
    }
}
