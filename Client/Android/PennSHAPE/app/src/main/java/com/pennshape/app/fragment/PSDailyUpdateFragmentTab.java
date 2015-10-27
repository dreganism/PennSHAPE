package com.pennshape.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pennshape.app.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PSDailyUpdateFragmentTab extends Fragment {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM.dd.yyyy", Locale.US);

    public PSDailyUpdateFragmentTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ps_fragment_daily_update, container, false);
        setupViewsAndControls(v);
        return v;
    }

    private void setupViewsAndControls(View view){
        //Date
        TextView dateTextView = (TextView)view.findViewById(R.id.date_text);
        Calendar today = Calendar.getInstance();
        dateTextView.setText(dateFormat.format(today.getTime()));
    }

}
