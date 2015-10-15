/**
 * Created by hasun on 10/14/15.
 */
package com.pennshape.app.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PSConfig {
    protected Integer startDate = 0;
    protected Integer endDate = 0;
    public PSConfig(JSONObject json) throws JSONException{
        startDate = Integer.parseInt((String)json.getString("start_date"));
        endDate = Integer.parseInt((String)json.getString("end_date"));
    }

}
