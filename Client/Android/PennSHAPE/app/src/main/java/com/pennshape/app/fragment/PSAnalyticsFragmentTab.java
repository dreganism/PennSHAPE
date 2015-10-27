/**
 * Created by hasun on 10/12/15.
 */
package com.pennshape.app.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
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
import com.pennshape.app.view.PSDailyDataMarkerView;
import com.pennshape.app.view.PSDatePickerView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PSAnalyticsFragmentTab extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ps_fragment_analytics, container, false);
        createProfileViews(v);
        setupControls(v);
        createChart(v);
        return v;
    }

    private void createProfileViews(View v) {
        PSUser user = PSDataStore.getInstance().getUser();
        TextView name = (TextView)v.findViewById(R.id.name);
        name.setText(user.getName());
        TextView weight = (TextView)v.findViewById(R.id.weight);
        weight.setText("Weight: "+user.getWeightDesc());
        TextView height = (TextView)v.findViewById(R.id.height);
        height.setText("Height: " + user.getHeightDesc());
        TextView age = (TextView)v.findViewById(R.id.age);
        age.setText("Age: " + user.getAgeDesc());
        TextView bwi = (TextView)v.findViewById(R.id.bmi);
        bwi.setText("BMI: " + user.getBMIDesc());
        //Set image
        ImageView imageView = (ImageView)v.findViewById(R.id.image);
        imageView.setImageResource(R.drawable.sample_photo_1);
    }

    private void setupControls(View view){
        PSDatePickerView datePickerView = (PSDatePickerView)view.findViewById(R.id.date_picker_view);
        datePickerView.setListener(new PSDatePickerView.PSDatePickerViewListener() {
            @Override
            public void onDateChanged() {
                createChart(getView());
            }
        });


        RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(getView()!=null) {
                    PSDatePickerView datePickerView = (PSDatePickerView) getView().findViewById(R.id.date_picker_view);
                    if (checkedId == R.id.radio_day) {
                        datePickerView.setByDay(true);
                    } else {
                        datePickerView.setByDay(false);
                    }
                }
            }
        });
    }

    private void createChart(View v) {
        BarChart chart = (BarChart)v.findViewById(R.id.barChart);
        chart.clear();
        PSDatePickerView datePickerView = (PSDatePickerView)v.findViewById(R.id.date_picker_view);
        PSUserDataCollection dataCollection = PSDataStore.getInstance().getUserDataCollection();
        ArrayList<BarEntry> entriesAgg =   new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        int idx = 0;
        boolean isDay = (((RadioGroup)v.findViewById(R.id.radio_group)).getCheckedRadioButtonId() == R.id.radio_day);
        for (Calendar cur = (Calendar)datePickerView.getStartDate().clone(); PSUtil.beforeCalender(cur, datePickerView.getEndDate()); cur.add(Calendar.DATE, 1)) {
            PSDailyData dailyData = dataCollection.getDailyData((int)(cur.getTimeInMillis()/1000));
            BarEntry ent = null;
            if (dailyData != null){
                ent = new BarEntry(dailyData.getFormula(), idx, dailyData);
            }
            if(ent!=null) {
                entriesAgg.add(ent);
            }
            if (isDay) {
                xVals.add("");
            }else {
                xVals.add(PSUtil.weekDays[idx]);
            }
            idx++;
        }

        BarDataSet setAgg = new BarDataSet(entriesAgg, PSDataStore.getInstance().getUser().getName());
        setAgg.setAxisDependency(YAxis.AxisDependency.LEFT);
        setAgg.setColor(getResources().getColor(R.color.ps_blue_sky));
        if(isDay) {
            setAgg.setBarSpacePercent(70);
        }

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(setAgg);

        //BarData
        BarData data = new BarData(xVals, dataSets);
        chart.setData(data);
        //Config chart
        chart.setDescription("");
        chart.setDrawValueAboveBar(false);
        chart.setDrawHighlightArrow(true);
        //Config axises
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        leftAxis.setAxisMaxValue(120f);
        rightAxis.setAxisMaxValue(120f);
        leftAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
        //Config limit line
        LimitLine ll = new LimitLine(60f);
        ll.setLineColor(Color.CYAN);
        ll.setLineWidth(1f);
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll);
        //Config marker
        PSDailyDataMarkerView markerView = new PSDailyDataMarkerView(v.getContext(), R.layout.ps_daily_data_marker_view);
        chart.setMarkerView(markerView);
        //Show data
        chart.animateY(600);
    }
}
