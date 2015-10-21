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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PSFriendsFragmentTab extends Fragment {
    protected Calendar startDate;
    protected Calendar endDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM.dd.yyyy", Locale.US);

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
        //createProfileViews(v);
        resetDates();
        refreshDateRangeDisplay(v);
        setupControls(v);
        createChart(v);
        return v;
    }


    private void createChart(View v) {
        BarChart chart = (BarChart)v.findViewById(R.id.barChart);
        ArrayList<PSUser> allUsers = PSDataStore.getInstance().getAllUsers();
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        int[] colors = getResources().getIntArray(R.array.chart_color_schema);
        for (int i= 0 ;i<allUsers.size();i++) {
            PSUser user = allUsers.get(i);
            PSUserDataCollection dataCollection = PSDataStore.getInstance().getUserDataCollection(user.getID());
            ArrayList<BarEntry> userEntries =   new ArrayList<BarEntry>();
            int idx = 0;
            for (Calendar cur = (Calendar)startDate.clone(); PSUtil.beforeCalender(cur, endDate); cur.add(Calendar.DATE, 1), idx++) {
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

    public void refreshDateRangeDisplay(View view) {
        TextView rangeDisplay = (TextView)view.findViewById(R.id.date_picker_display);
        if(rangeDisplay!=null) {
            rangeDisplay.setText(dateFormat.format(startDate.getTime()) + " -- " + dateFormat.format(endDate.getTime()));
        }
    }

    private void setupControls(View view){
        ImageButton leftArrow = (ImageButton)view.findViewById(R.id.left_arrow);
        ImageButton rightArrow = (ImageButton)view.findViewById(R.id.right_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousWeek();
                refreshDateRangeDisplay(getView());
                createChart(getView());
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWeek();
                refreshDateRangeDisplay(getView());
                createChart(getView());
            }
        });
    }

    private void resetDates(){
        Calendar now = Calendar.getInstance(Locale.US);
        int dayOfWeek = (now.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        startDate = Calendar.getInstance(Locale.US);
        startDate.add(Calendar.DATE, -dayOfWeek);
        endDate = Calendar.getInstance(Locale.US);
        endDate.add(Calendar.DATE, 6 - dayOfWeek);
    }

    private void previousWeek(){
        startDate.add(Calendar.DATE, -7);
        endDate.add(Calendar.DATE, -7);
    }

    private void nextWeek(){
        startDate.add(Calendar.DATE, 7);
        endDate.add(Calendar.DATE, 7);
    }
}
