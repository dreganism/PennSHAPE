package com.pennshape.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.model.PSMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.pennshape.app.R;
import com.pennshape.app.model.PSUser;

import org.w3c.dom.Text;

/**
 * Created by hasun on 11/19/15.
 */
public class PSMessagesArrayAdapter extends ArrayAdapter<PSMessage> {
    private ArrayList<PSMessage> messageList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd HH:mm", Locale.US);
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
        TextView timeView = (TextView)convertView.findViewById(R.id.time);
        PSMessage message = getItem(position);
        PSUser user = PSDataStore.getInstance().getUser(message.getUid());
        String sender = "Penn Fit";
        if(user != null && user.themeColor!=0){
            sender = user.getName();
            senderView.setTextColor(user.themeColor);
        }else{
            senderView.setTextColor(getContext().getResources().getColor(R.color.ps_purple));
        }
        senderView.setText(sender);
        textView.setText(message.getMessage());
        Long unix = message.getTime();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(unix);
        timeView.setText(dateFormat.format(date.getTime()));
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
