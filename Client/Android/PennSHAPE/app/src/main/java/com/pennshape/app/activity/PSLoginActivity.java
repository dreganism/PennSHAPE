package com.pennshape.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pennshape.app.R;
import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.request.PSHttpTaskRequest;
import com.pennshape.app.request.PSLoginTaskRequest;
import com.pennshape.app.request.PSUserDataTaskRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class PSLoginActivity extends Activity implements PSHttpTaskRequest.PSHttpTaskRequestHandler{
    EditText userName;
    Button loginButton;
    ProgressDialog progress;
    String intentExtraString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_activity_login);
        userName = (EditText)findViewById(R.id.loginUserNameEditText);
        loginButton = (Button)findViewById(R.id.button);
        //Get intent extra
        Intent i = getIntent();
        intentExtraString = i.getStringExtra(Intent.EXTRA_TEXT);
        //Need login?
        checkUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ps_menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        if(userName.getText().toString().equals("4Test")){
            showSampleData();
        }else{
            login();
            if(progress!= null) progress.dismiss();
            progress = ProgressDialog.show(this, "Loading...", "Please wait", true);
        }
    }

    private void checkUser(){
        if(PSDataStore.getInstance().dataReady()){
            intentMainActivity();
            return;
        }
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.ps_pref_name), Context.MODE_PRIVATE);
        String userID = sharedPref.getString(getString(R.string.ps_pref_key_id), null);
        String groupID = sharedPref.getString(getString(R.string.ps_pref_key_group), null);
        if(userID!=null && userID.length()>0 && groupID!=null && groupID.length()>0 ) {
            progress = ProgressDialog.show(this, "Loading...", "Please wait", true);
            pullUserData(userID);
        }
        PSDataStore.getInstance().setGroup(groupID);
    }

    private void persistUserID(String userID, String groupID) {
        PSDataStore.getInstance().setGroup(groupID);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.ps_pref_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.ps_pref_key_id), userID);
        editor.putString(getString(R.string.ps_pref_key_group), groupID);
        editor.apply();
    }

    private void showSampleData() {
        //Using sample data
        try {
            AssetManager am = getApplicationContext().getAssets();
            InputStream is = am.open("sample_g4w3.json");
            PSDataStore.getInstance().reloadFromInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        intentMainActivity();
    }

    private void displayError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void login(){
        PSLoginTaskRequest request = new PSLoginTaskRequest();
        request.setUserName(userName.getText().toString());
        request.handler = this;
        request.run();
    }

    private void pullUserData(String userID) {
        PSUserDataTaskRequest request = new PSUserDataTaskRequest();
        request.setUserID(userID);
        request.handler = this;
        request.run();
    }

    private void intentMainActivity(){
        Intent intent = new Intent(this, PSMainActivity.class);
        if(intentExtraString!=null) {
            intent.putExtra(Intent.EXTRA_TEXT, intentExtraString);
        }
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onSuccess(PSHttpTaskRequest request, Object result) {
        if(request instanceof PSLoginTaskRequest) {
            String userID = (String)result;
            String groupID = ((PSLoginTaskRequest)request).getGroup();
            if(userID.length()==0 || groupID.length()==0){
                displayError("Invalid user ID or group");
                if(progress!=null) progress.dismiss();
            }else{
                persistUserID(userID, groupID);
                pullUserData(userID);
            }
        }else if(request instanceof PSUserDataTaskRequest) {
            if(progress!=null) progress.dismiss();
            JSONObject userData = (JSONObject)result;
            try {
                PSDataStore.getInstance().reloadFronJson(userData);
            } catch (JSONException e) {
                displayError("User data parse failure");
                return;
            }
            intentMainActivity();
        }
    }

    @Override
    public void onFailure(PSHttpTaskRequest request, String error) {
        if(progress!=null) progress.dismiss();
        displayError(error);
    }
}
