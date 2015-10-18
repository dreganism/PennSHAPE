/**
 * Created by hasun on 10/12/15.
 */
package com.pennshape.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.pennshape.app.R;
import com.pennshape.app.model.PSDailyData;
import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.model.PSUser;
import com.pennshape.app.model.PSUserDataCollection;
import com.pennshape.app.model.PSUtil;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PSAnalyticsFragmentTab extends Fragment {
    protected Calendar startDate;
    protected Calendar endDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM.dd.yyyy", Locale.US);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetDates();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ps_fragment_analytics, container, false);
        createProfileViews(v);
        createChart(v);
        refreshDateRangeDisplay(v);
        createControls(v);
        return v;
    }

    private void resetDates(){
        Calendar now = Calendar.getInstance(Locale.US);
        int dayOfWeek = (now.get(Calendar.DAY_OF_WEEK)+5)%7;
        startDate = Calendar.getInstance(Locale.US);
        startDate.add(Calendar.DATE, -dayOfWeek);
        endDate = Calendar.getInstance(Locale.US);
        endDate.add(Calendar.DATE, 6 - dayOfWeek);
    }

    private void createProfileViews(View v) {
        PSUser user = PSDataStore.getInstance().getUser();
        TextView name = (TextView)v.findViewById(R.id.name);
        name.setText(user.getName());
        TextView weight = (TextView)v.findViewById(R.id.weight);
        weight.setText("W: "+user.getWeightDesc());
        TextView height = (TextView)v.findViewById(R.id.height);
        height.setText("H: " + user.getHeightDesc());
        TextView age = (TextView)v.findViewById(R.id.age);
        age.setText("A: " + user.getAgeDesc());
        TextView bwi = (TextView)v.findViewById(R.id.bmi);
        bwi.setText("BWI: " + user.getBMIDesc());
    }

    private void createControls(View view){
        ImageButton leftArrow = (ImageButton)view.findViewById(R.id.left_arrow);
        ImageButton rightArrow = (ImageButton)view.findViewById(R.id.right_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate.add(Calendar.DATE, -7);
                endDate.add(Calendar.DATE, -7);
                refreshDateRangeDisplay(null);
                createChart(getView());
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate.add(Calendar.DATE, 7);
                endDate.add(Calendar.DATE, 7);
                refreshDateRangeDisplay(null);
                createChart(getView());
            }
        });
    }

    public void refreshDateRangeDisplay(View view) {
        if(view == null)
            view = getView();
        TextView rangeDisplay = (TextView)view.findViewById(R.id.date_picker_display);
        rangeDisplay.setText(dateFormat.format(startDate.getTime())+" -- "+dateFormat.format(endDate.getTime()));
    }

    private void createChart(View v) {
        BarChart chart = (BarChart)v.findViewById(R.id.barChart);
        PSUserDataCollection dataCollection = PSDataStore.getInstance().getUserDataCollection();
        ArrayList<BarEntry> entriesAgg =   new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        int idx = 0;
        for (Calendar cur = (Calendar)startDate.clone(); PSUtil.beforeCalender(cur, endDate); cur.add(Calendar.DATE, 1)) {
            PSDailyData dailyData = dataCollection.getDailyDataByTimeInSeconds((int)(cur.getTimeInMillis()/1000));
            BarEntry ent = new BarEntry(0f, idx);
            if (dailyData != null){
                ent = new BarEntry(dailyData.getFormula(), idx);
            }
            entriesAgg.add(ent);
            xVals.add(Integer.toString(idx++ + 1));
        }

        BarDataSet setAgg = new BarDataSet(entriesAgg, PSDataStore.getInstance().getUser().getName());
        setAgg.setAxisDependency(YAxis.AxisDependency.LEFT);
        setAgg.setColor(getResources().getColor(R.color.ps_blue_sky));

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(setAgg);

        BarData data = new BarData(xVals, dataSets);
        chart.setData(data);
        chart.setTouchEnabled(false);
        chart.setDescription("");
        chart.invalidate(); // refresh
    }
}
