/**
 * Created by hasun on 10/13/15.
 */
package com.pennshape.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.pennshape.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PSDataStore {
    private static PSDataStore sharedInstance = new PSDataStore();
    protected String userID = "";
    protected String group = "";
    protected HashMap<String, PSUser> groupMembers;
    protected HashMap<String, PSUserDataCollection> groupMembersData;
    protected Calendar lastUpdate;
    protected PSConfig config;
    protected PSDatabaseHelper dbHelper;
    protected Context appContext;
    public static PSDataStore getInstance() {
        return sharedInstance;
    }
    private PSDataStore(){}

    public void setAppContext(Context context){
        appContext = context;
    }

    public void reloadFromInputStream(InputStream is) {
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonString = new String(buffer);
            reloadFronJson(new JSONObject(jsonString));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void reloadFronJson(JSONObject json) throws JSONException{
        //load user id
        userID = json.getString("uid");
        //load all users
        loadAllUsers(json.getJSONArray("group"));
        //load all data
        loadAllData(json.getJSONObject("data"));
        //load config
        loadConfig(json.getJSONObject("config"));
        //update timestamp
        lastUpdate = Calendar.getInstance();
    }

    public boolean expired(){
        if(lastUpdate != null){
            Calendar now = Calendar.getInstance();
            if((now.getTimeInMillis()-lastUpdate.getTimeInMillis())>60000){
                return true;
            }else{
                return false;
            }
        }
        return true;
    }

    protected void loadAllUsers(JSONArray array) throws JSONException{
        groupMembers = new HashMap<String, PSUser>();
        int[] colors = appContext.getResources().getIntArray(R.array.chart_color_schema);
        int nColors = colors.length;
        for (int i = 0; i < array.length(); i++) {
            PSUser user = new PSUser(array.getJSONObject(i));
            groupMembers.put(user.getID(), user);
            //set display color
            user.themeColor = colors[i%nColors];
        }
    }

    protected void loadAllData(JSONObject json) throws JSONException{
        groupMembersData = new HashMap<String, PSUserDataCollection>();
        Iterator<?> userIDs = json.keys();
        while (userIDs.hasNext()) {
            String uid = (String) userIDs.next();
            PSUserDataCollection collection = new PSUserDataCollection(uid, json.getJSONArray(uid));
            groupMembersData.put(uid, collection);
        }
    }

    protected void loadConfig(JSONObject json) throws JSONException{
        config = new PSConfig(json);
    }

    public PSUserDataCollection getUserDataCollection() {
        return groupMembersData.get(userID);
    }

    public PSUserDataCollection getUserDataCollection(String uid) {
        return groupMembersData.get(uid);
    }

    public String getUserID() {
        return userID;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public PSUser getUser() {
        return groupMembers.get(userID);
    }

    public boolean dataReady(){
        if(groupMembersData!=null && groupMembersData.size()>0)
            return true;
        return  false;
    }

    public PSUser getUser(String userID) {
        return groupMembers.get(userID);
    }

    public ArrayList<PSUser> getAllUsers() {
        ArrayList<PSUser> users = new ArrayList<PSUser>();
        //add self first
        users.add(groupMembers.get(getUserID()));
        for (PSUser user : groupMembers.values()) {
            if(!user.getID().equals(getUserID())) {
                users.add(user);
            }
        }
        return users;
    }

    /* *** *** *** *** *** Message Database *** *** *** *** *** */
    public void initDatabase(Context context){
        if(dbHelper==null) {
            dbHelper = new PSDatabaseHelper(context);
        }
    }

    public int lastMessageID(){
        Cursor cursor = dbHelper.getLast();
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(PSDatabaseHelper.COL_ID));
        }
        return -1;
    }

    public void saveMessageArray(JSONArray array) {
        for (int i =0;i<array.length();i++){
            try {
                JSONObject json = array.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(PSDatabaseHelper.COL_ID, json.getInt("id"));
                contentValues.put(PSDatabaseHelper.COL_UID, json.getString("uid"));
                contentValues.put(PSDatabaseHelper.COL_TIME, json.getLong("time"));
                contentValues.put(PSDatabaseHelper.COL_MSG, json.getString("msg"));
                contentValues.put(PSDatabaseHelper.COL_GROUP_ID, json.getInt("groupid"));
                dbHelper.insertValues(contentValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<PSMessage> getRecentMessages() {
        ArrayList<PSMessage> messages = new ArrayList<PSMessage>();
        Cursor cursor = dbHelper.getDataTotal(200);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            PSMessage message = new PSMessage(
                    cursor.getInt(cursor.getColumnIndex(PSDatabaseHelper.COL_ID)),
                    cursor.getString(cursor.getColumnIndex(PSDatabaseHelper.COL_UID)),
                    cursor.getInt(cursor.getColumnIndex(PSDatabaseHelper.COL_GROUP_ID)),
                    cursor.getString(cursor.getColumnIndex(PSDatabaseHelper.COL_MSG)),
                    cursor.getLong(cursor.getColumnIndex(PSDatabaseHelper.COL_TIME))
            );
            messages.add(message);
            cursor.moveToNext();
        }
        return messages;
    }
}
