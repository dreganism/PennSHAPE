/**
 * Created by hasun on 10/12/15.
 */
package com.pennshape.app.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
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
import com.pennshape.app.request.PSDataUploadTaskRequest;
import com.pennshape.app.request.PSHttpTaskRequest;
import com.pennshape.app.request.PSUserDataTaskRequest;
import com.pennshape.app.request.PSUserProfileUploadTaskRequest;
import com.pennshape.app.view.PSDailyDataMarkerView;
import com.pennshape.app.view.PSDatePickerView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PSAnalyticsFragmentTab extends Fragment implements PSHttpTaskRequest.PSHttpTaskRequestHandler{
    private ProgressDialog progress;
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
        TextView fav = (TextView)v.findViewById(R.id.exercise);
        fav.setText("♥ "+ user.getFavorite());
        //Set image
        ImageView imageView = (ImageView)v.findViewById(R.id.image);
        Picasso.with(v.getContext())
                .load(user.getPhoto())
                .into(imageView);
    }

    private void setupControls(View view){
        PSDatePickerView datePickerView = (PSDatePickerView)view.findViewById(R.id.date_picker_view);
        datePickerView.setListener(new PSDatePickerView.PSDatePickerViewListener() {
            @Override
            public void onDateChanged() {
                createChart(getView());
            }
        });
        Button changeButton = (Button)view.findViewById(R.id.change);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        LayoutInflater inflater = LayoutInflater.from(getView().getContext());
        View alertView = inflater.inflate(R.layout.ps_favorite_exercise_alert_dialog, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getView().getContext());
        alertBuilder.setView(alertView);
        final EditText editText = (EditText)alertView.findViewById(R.id.editText);
        alertBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String input = editText.getText().toString();
                        submitFavoriteExercise(input);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();


    }

    private void submitFavoriteExercise(String fav){
        PSUserProfileUploadTaskRequest request = new PSUserProfileUploadTaskRequest();
        request.setUserID(PSDataStore.getInstance().getUserID());
        request.setFavorite(fav);
        request.handler = this;
        request.execute();
        progress = ProgressDialog.show(getView().getContext(), "Loading...", "Please wait", true);
    }

    private void createChart(View v) {
        BarChart chart = (BarChart)v.findViewById(R.id.barChart);
        chart.clear();
        PSDatePickerView datePickerView = (PSDatePickerView)v.findViewById(R.id.date_picker_view);
        PSUserDataCollection dataCollection = PSDataStore.getInstance().getUserDataCollection();
        ArrayList<BarEntry> entriesAgg =   new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        int idx = 0;
        for (Calendar cur = (Calendar)datePickerView.getStartDate().clone(); PSUtil.beforeCalender(cur, datePickerView.getEndDate()); cur.add(Calendar.DATE, 1)) {
            PSDailyData dailyData = dataCollection.getDailyData(cur.getTimeInMillis());
            BarEntry ent = null;
            if (dailyData != null){
                ent = new BarEntry(dailyData.getFormula(), idx, dailyData);
            }
            if(ent!=null) {
                entriesAgg.add(ent);
            }
            xVals.add(PSUtil.weekDays[idx]);

            idx++;
        }

        BarDataSet setAgg = new BarDataSet(entriesAgg, PSDataStore.getInstance().getUser().getName());
        setAgg.setAxisDependency(YAxis.AxisDependency.LEFT);
        setAgg.setColor(getResources().getColor(R.color.ps_blue_sky));

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(setAgg);

        //BarData
        BarData data = new BarData(xVals, dataSets);
        chart.setData(data);
        //Config chart
        chart.setDescription("");
        chart.setDrawHighlightArrow(true);
        //Config axises
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
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
        //Legend
        Legend l = chart.getLegend();
        l.setEnabled(false);
        //Show data
        chart.animateY(600);
    }

    private void displayError(String message) {
        new AlertDialog.Builder(getView().getContext())
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(PSDataStore.getInstance().expired()){
            refreshData();
            progress = ProgressDialog.show(getView().getContext(), "Loading...", "Please wait", true);
        }
    }

    @Override
    public void onSuccess(PSHttpTaskRequest request, Object result) {
        if(request instanceof PSUserProfileUploadTaskRequest){
            refreshData();
        }else if(request instanceof PSUserDataTaskRequest){
            if(progress!=null) progress.dismiss();
            JSONObject userData = (JSONObject)result;
            try {
                PSDataStore.getInstance().reloadFronJson(userData);
                createProfileViews(getView());
                createChart(getView());
            } catch (JSONException e) {
                displayError("User data parse failure");
            }
        }
    }

    @Override
    public void onFailure(PSHttpTaskRequest request, String error) {
        if(progress!=null) progress.dismiss();
        displayError(error);
    }

    private void refreshData(){
        PSUserDataTaskRequest request = new PSUserDataTaskRequest();
        request.setUserID(PSDataStore.getInstance().getUserID());
        request.handler = this;
        request.execute();
    }
}
