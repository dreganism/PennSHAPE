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
    private SeekBar seekBarC1;
    private SeekBar seekBarC2;
    private SeekBar seekBarC3;
    private TextView displayC1;
    private TextView displayC2;
    private TextView displayC3;
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(seekBar == seekBarC1){
                displayC1.setText(progress+"'");
            }else if(seekBar == seekBarC2){
                displayC2.setText(progress+"'");
            }else if(seekBar == seekBarC3){
                displayC3.setText(progress+"'");
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };

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

        //Seek bars
        seekBarC1 = (SeekBar)view.findViewById(R.id.volume_bar_c1);
        seekBarC2 = (SeekBar)view.findViewById(R.id.volume_bar_c2);
        seekBarC3 = (SeekBar)view.findViewById(R.id.volume_bar_c3);

        //Display texts
        displayC1 = (TextView)view.findViewById(R.id.c1_display);
        displayC2 = (TextView)view.findViewById(R.id.c2_display);
        displayC3 = (TextView)view.findViewById(R.id.c3_display);

        //Listeners
        seekBarC1.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarC2.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarC3.setOnSeekBarChangeListener(seekBarChangeListener);
    }

}
