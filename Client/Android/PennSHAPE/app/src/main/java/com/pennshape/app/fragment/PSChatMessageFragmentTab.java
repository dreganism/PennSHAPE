package com.pennshape.app.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pennshape.app.R;
import com.pennshape.app.adapter.PSMessagesArrayAdapter;
import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.model.PSMessage;
import com.pennshape.app.request.PSHttpTaskRequest;
import com.pennshape.app.request.PSMessagePullTaskRequest;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PSChatMessageFragmentTab extends Fragment implements PSHttpTaskRequest.PSHttpTaskRequestHandler{
    private ProgressDialog progress;
    private PSMessagesArrayAdapter messagesAdapter;

    public PSChatMessageFragmentTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ps_fragment_chat_message, container, false);
        return v;
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


    @Override
    public void onSuccess(PSHttpTaskRequest request, Object result) {
        if(request instanceof PSMessagePullTaskRequest){
            PSDataStore.getInstance().saveMessageArray((JSONArray)result);
            showMessages(getView());
        }
        if(progress!= null) progress.dismiss();
    }

    @Override
    public void onFailure(PSHttpTaskRequest request, String error) {
        if(progress!= null) progress.dismiss();
    }
}
