package com.pennshape.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.pennshape.app.R;
import com.pennshape.app.adapter.PSSport;
import com.pennshape.app.adapter.PSSportArrayAdapter;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
        //Spinners
        int spinnerIDs[] = {
                R.id.spinner11, R.id.spinner12, R.id.spinner13,
                R.id.spinner21, R.id.spinner22, R.id.spinner23,
                R.id.spinner31, R.id.spinner32, R.id.spinner33
        };
        PSSportArrayAdapter.SportCategory categories[] = {
                PSSportArrayAdapter.SportCategory.Mod,
                PSSportArrayAdapter.SportCategory.Vig,
                PSSportArrayAdapter.SportCategory.Mus
        };
        for(int i = 0; i< 9; i++){
            Spinner spinner = (Spinner)view.findViewById(spinnerIDs[i]);
            spinner.setAdapter(
                    new PSSportArrayAdapter(view.getContext(), R.layout.ps_spinner_sport, categories[i/3])
            );
        }
    }

}
