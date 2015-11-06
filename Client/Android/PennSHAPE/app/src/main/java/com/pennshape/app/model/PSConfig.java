/**
 * Created by hasun on 10/14/15.
 */
package com.pennshape.app.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PSConfig {
    protected Long startDate = 0L;
    protected Long endDate = 0L;
    public PSConfig(JSONObject json) throws JSONException{
        startDate = json.getLong("start_date");
        endDate = json.getLong("end_date");
    }

}
