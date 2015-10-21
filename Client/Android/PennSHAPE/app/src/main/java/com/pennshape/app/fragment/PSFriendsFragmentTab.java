package com.pennshape.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.pennshape.app.R;
import com.pennshape.app.model.PSDailyData;
import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.model.PSUser;
import com.pennshape.app.model.PSUserDataCollection;
import com.pennshape.app.model.PSUtil;
import com.pennshape.app.view.PSDatePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PSFriendsFragmentTab extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public PSFriendsFragmentTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ps_fragment_friends, container, false);
        setupControls(v);
        createChart(v);
        return v;
    }


    private void createChart(View v) {
        BarChart chart = (BarChart)v.findViewById(R.id.barChart);
        PSDatePickerView datePickerView = (PSDatePickerView)v.findViewById(R.id.date_picker_view);
        ArrayList<PSUser> allUsers = PSDataStore.getInstance().getAllUsers();
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        int[] colors = getResources().getIntArray(R.array.chart_color_schema);
        for (int i= 0 ;i<allUsers.size();i++) {
            PSUser user = allUsers.get(i);
            PSUserDataCollection dataCollection = PSDataStore.getInstance().getUserDataCollection(user.getID());
            ArrayList<BarEntry> userEntries =   new ArrayList<BarEntry>();
            int idx = 0;
            for (Calendar cur = (Calendar)datePickerView.getStartDate().clone(); PSUtil.beforeCalender(cur, datePickerView.getEndDate()); cur.add(Calendar.DATE, 1), idx++) {
                PSDailyData dailyData = dataCollection.getDailyData((int)(cur.getTimeInMillis()/1000));
                BarEntry ent = new BarEntry(0f, idx);
                if (dailyData != null){
                    ent = new BarEntry(dailyData.getFormula(), idx);
                }
                userEntries.add(ent);
            }
            BarDataSet userDataSet = new BarDataSet(userEntries, user.getName());
            userDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            userDataSet.setColor(colors[i]);
            dataSets.add(userDataSet);
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (String weekDay : PSUtil.weekDays){
            xVals.add(weekDay);
        }
        BarData data = new BarData(xVals, dataSets);
        chart.setData(data);
        chart.setTouchEnabled(false);
        chart.setDescription("");
        chart.invalidate(); // refresh
    }

    private void setupControls(View view){
        PSDatePickerView datePickerView = (PSDatePickerView)view.findViewById(R.id.date_picker_view);
        datePickerView.setListener(new PSDatePickerView.PSDatePickerViewListener() {
            @Override
            public void onDateChanged() {
                createChart(getView());
            }
        });
    }
}
