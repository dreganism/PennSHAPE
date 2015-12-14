package com.pennshape.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pennshape.app.R;
import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.request.PSHttpTaskRequest;
import com.pennshape.app.request.PSMessagePullTaskRequest;
import com.pennshape.app.request.PSMessageSendTaskRequest;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PSAdminActivity extends Activity implements PSHttpTaskRequest.PSHttpTaskRequestHandler{
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private ProgressDialog progress;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_activity_admin);
        editText = (EditText)findViewById(R.id.edit);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.send){
            String message = editText.getText().toString();
            if(message.length()>0){
                postMessage(message);
            }
        }else if(view.getId() == R.id.clear){
            editText.setText("");
        }
    }

    private void postMessage(String message){
        PSMessageSendTaskRequest request = new PSMessageSendTaskRequest();
        request.setUserID("0");
        request.setGroupid("0");
        request.setMsg(message);
        request.setTime(dateFormat.format(Calendar.getInstance().getTime()));
        request.handler = this;
        request.run();
        if(progress!= null) progress.dismiss();
        progress = ProgressDialog.show(this, "Loading...", "Please wait", true);
    }

    @Override
    public void onSuccess(PSHttpTaskRequest request, Object result) {
        if(request instanceof PSMessageSendTaskRequest){
            new AlertDialog.Builder(this)
                    .setTitle("Message Sent")
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            editText.setText("");
        }
        if(progress!= null) progress.dismiss();
    }

    @Override
    public void onFailure(PSHttpTaskRequest request, String error) {
        if(progress!= null) progress.dismiss();
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(error)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
