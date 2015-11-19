package com.pennshape.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.model.PSMessage;

import java.util.ArrayList;

import com.pennshape.app.R;
import com.pennshape.app.model.PSUser;

import org.w3c.dom.Text;

/**
 * Created by hasun on 11/19/15.
 */
public class PSMessagesArrayAdapter extends ArrayAdapter<PSMessage> {
    private ArrayList<PSMessage> messageList;
    public PSMessagesArrayAdapter(Context context, int resource, ArrayList<PSMessage> objects) {
        super(context, resource, objects);
        messageList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = View.inflate(getContext(), R.layout.ps_message_list_item, null);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.message);
        TextView senderView = (TextView)convertView.findViewById(R.id.sender);
        PSMessage message = getItem(position);
        PSUser user = PSDataStore.getInstance().getUser(message.getUid());
        String sender = "Penn Fit";
        if(user != null){
            sender = user.getName();
        }
        senderView.setText(sender);
        textView.setText(message.getMessage());
        return convertView;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }
    @Override
    public PSMessage getItem(int position) {
        return messageList.get(position);
    }
}
