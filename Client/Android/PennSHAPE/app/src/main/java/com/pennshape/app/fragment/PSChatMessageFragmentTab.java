package com.pennshape.app.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.pennshape.app.R;
import com.pennshape.app.adapter.PSMessagesArrayAdapter;
import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.model.PSMessage;
import com.pennshape.app.request.PSHttpTaskRequest;
import com.pennshape.app.request.PSMessagePullTaskRequest;
import com.pennshape.app.request.PSMessageSendTaskRequest;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class PSChatMessageFragmentTab extends Fragment implements PSHttpTaskRequest.PSHttpTaskRequestHandler{
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private ProgressDialog progress;
    private PSMessagesArrayAdapter messagesAdapter;

    public PSChatMessageFragmentTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ps_fragment_chat_message, container, false);
        setupViewsAndControls(v);
        return v;
    }

    private void setupViewsAndControls(View view){
        Button send = (Button)view.findViewById(R.id.chat);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
    }

    private void showInputDialog() {
        LayoutInflater inflater = LayoutInflater.from(getView().getContext());
        View alertView = inflater.inflate(R.layout.ps_alert_dialog_message, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getView().getContext());
        alertBuilder.setView(alertView);
        final EditText editText = (EditText)alertView.findViewById(R.id.editMessage);
        alertBuilder.setPositiveButton("Send",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String input = editText.getText().toString();
                        if (input.length()>0) {
                            sendMessage(input);
                        }else{
                            displayError("Message cannot be empty");
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();


    }

    private void sendMessage(String message) {
        PSMessageSendTaskRequest request = new PSMessageSendTaskRequest();
        request.setUserID(PSDataStore.getInstance().getUserID());
        request.setGroupid(PSDataStore.getInstance().getGroup());
        request.setMsg(message);
        request.setTime(dateFormat.format(Calendar.getInstance().getTime()));
        request.handler = this;
        request.run();
        if(progress!= null) progress.dismiss();
        progress = ProgressDialog.show(getView().getContext(), "Loading...", "Please wait", true);
    }

    public void onResume(){
        super.onResume();
        pullMessages();
    }

    public void showMessages(View view){
        ArrayList<PSMessage> messageList = PSDataStore.getInstance().getRecentMessages();
        messagesAdapter = new PSMessagesArrayAdapter(view.getContext(), R.layout.ps_message_list_item, messageList);
        ListView listView = (ListView)view.findViewById(R.id.listView);
        listView.setAdapter(messagesAdapter);
    }

    public void pullMessages(){
        if(progress!= null) progress.dismiss();
        progress = ProgressDialog.show(getView().getContext(), "Loading...", "Please wait", true);
        PSMessagePullTaskRequest request = new PSMessagePullTaskRequest();
        request.setGroupID(PSDataStore.getInstance().getGroup());
        int lastID = PSDataStore.getInstance().lastMessageID();
        if(lastID>0){
            request.setFrom(""+lastID);
        }
        request.handler = this;
        request.run();
    }

    private void displayError(String message) {
        new AlertDialog.Builder(getView().getContext())
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onSuccess(PSHttpTaskRequest request, Object result) {
        if(request instanceof PSMessagePullTaskRequest){
            PSDataStore.getInstance().saveMessageArray((JSONArray)result);
            showMessages(getView());
            if(progress!= null) progress.dismiss();
        }else if(request instanceof PSMessageSendTaskRequest){
            pullMessages();
        }
    }

    @Override
    public void onFailure(PSHttpTaskRequest request, String error) {
        if(progress!= null) progress.dismiss();
    }
}
