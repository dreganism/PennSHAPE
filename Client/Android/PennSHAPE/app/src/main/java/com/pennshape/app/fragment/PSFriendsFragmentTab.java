package com.pennshape.app.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
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
import com.pennshape.app.request.PSHttpTaskRequest;
import com.pennshape.app.request.PSUserDataTaskRequest;
import com.pennshape.app.view.PSDailyDataMarkerView;
import com.pennshape.app.view.PSDatePickerView;
import com.pennshape.app.view.PSUserInfoSelectionView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class PSFriendsFragmentTab extends Fragment
        implements
        PSUserInfoSelectionView.PSUserInfoSelectionViewListener,
        PSHttpTaskRequest.PSHttpTaskRequestHandler{
    private ArrayList<PSUserInfoSelectionView> userSelectionViews= new ArrayList<PSUserInfoSelectionView>();
    private ProgressDialog progress;
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

    @Override
    public void onResume(){
        super.onResume();
        if(PSDataStore.getInstance().expired()){
            PSUserDataTaskRequest request = new PSUserDataTaskRequest();
            request.setUserID(PSDataStore.getInstance().getUserID());
            request.handler = this;
            request.run();
            if(progress!= null) progress.dismiss();
            progress = ProgressDialog.show(getView().getContext(), "Loading...", "Please wait", true);
        }
    }


    private void createChart(View v) {
        BarChart chart = (BarChart)v.findViewById(R.id.barChart);
        chart.clear();
        PSDatePickerView datePickerView = (PSDatePickerView)v.findViewById(R.id.date_picker_view);
        ArrayList<PSUserInfoSelectionView> allUsers = selectedUsers();
        if(allUsers.size()==0){
            chart.invalidate();
            return;
        }
        //PSDataStore.getInstance().getAllUsers();
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        for (int i= 0 ;i<allUsers.size();i++) {
            PSUser user = allUsers.get(i).selectedUser();
            PSUserDataCollection dataCollection = PSDataStore.getInstance().getUserDataCollection(user.getID());
            ArrayList<BarEntry> userEntries =   new ArrayList<BarEntry>();
            int idx = 0;
            for (Calendar cur = (Calendar)datePickerView.getStartDate().clone(); PSUtil.beforeCalender(cur, datePickerView.getEndDate()); cur.add(Calendar.DATE, 1), idx++) {
                PSDailyData dailyData = dataCollection.getDailyData(cur.getTimeInMillis());
                BarEntry ent = null;
                if (dailyData != null){
                    ent = new BarEntry(dailyData.getFormula(), idx, dailyData);
                }else{
                    ent = new BarEntry(0, idx, null);
                }
                userEntries.add(ent);
            }
            BarDataSet userDataSet = new BarDataSet(userEntries, user.getName());
            userDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            userDataSet.setColor(allUsers.get(i).getUserColor());
            dataSets.add(userDataSet);
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (String weekDay : PSUtil.weekDays){
            xVals.add(weekDay);
        }
        //BarData
        BarData data = new BarData(xVals, dataSets);
        chart.setData(data);
        //Config chart
        chart.setDescription("");
        chart.setVisibleXRangeMaximum(12);
        chart.setDrawHighlightArrow(true);
        int viewPortIndex = getCenterIdx(datePickerView.getStartDate(), datePickerView.getEndDate(), allUsers.size());
        chart.centerViewTo(viewPortIndex, 0, YAxis.AxisDependency.LEFT);
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
        //Show data
        chart.animateY(600);
    }

    protected int getCenterIdx(final Calendar start, final Calendar end, final int users){
        Calendar now = Calendar.getInstance();
        if(now.get(Calendar.DAY_OF_YEAR)>=start.get(Calendar.DAY_OF_YEAR) &&
           now.get(Calendar.DAY_OF_YEAR) <= end.get(Calendar.DAY_OF_YEAR)){
            return (now.get(Calendar.DAY_OF_WEEK)+5)%7*users+users/2;
        }
        return 0;
    }

    private void setupControls(View view){
        //user selection
        userSelectionViews.clear();
        userSelectionViews.add((PSUserInfoSelectionView)view.findViewById(R.id.user_selection_1));
        userSelectionViews.add((PSUserInfoSelectionView)view.findViewById(R.id.user_selection_2));
        userSelectionViews.add((PSUserInfoSelectionView)view.findViewById(R.id.user_selection_3));
        userSelectionViews.add((PSUserInfoSelectionView)view.findViewById(R.id.user_selection_4));
        int[] colors = getResources().getIntArray(R.array.chart_color_schema);
        ArrayList<PSUser> allUsers = PSDataStore.getInstance().getAllUsers();
        int idx = 0;
        for(PSUserInfoSelectionView selectionView : userSelectionViews){
            selectionView.mListener = this;
            if(idx<allUsers.size()) {
                selectionView.setUser(allUsers.get(idx));
                selectionView.setUserColor(colors[idx]);
            }
            idx++;
        }
        //date picker
        PSDatePickerView datePickerView = (PSDatePickerView)view.findViewById(R.id.date_picker_view);
        datePickerView.setByDay(false);
        datePickerView.setListener(new PSDatePickerView.PSDatePickerViewListener() {
            @Override
            public void onDateChanged() {
                createChart(getView());
            }
        });
    }

    private ArrayList<PSUserInfoSelectionView> selectedUsers() {
        ArrayList<PSUserInfoSelectionView> selectedUsers = new ArrayList<PSUserInfoSelectionView>();
        for(PSUserInfoSelectionView selectionView : userSelectionViews){
            PSUser user = selectionView.selectedUser();
            if(user != null){
                selectedUsers.add(selectionView);
            }
        }
        return selectedUsers;
    }

    public void onSelectionChanged() {
        createChart(getView());
    }

    @Override
    public void onSuccess(PSHttpTaskRequest request, Object result) {
        if(request instanceof PSUserDataTaskRequest){
            if(progress!=null) progress.dismiss();
            JSONObject userData = (JSONObject)result;
            try {
                PSDataStore.getInstance().reloadFronJson(userData);
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

    private void displayError(String message) {
        new AlertDialog.Builder(getView().getContext())
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
