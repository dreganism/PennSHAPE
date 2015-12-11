package com.pennshape.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pennshape.app.R;

public class PSAdminActivity extends Activity {
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

    }
}
