package com.pennshape.app.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pennshape.app.R;
import com.pennshape.app.adapter.PSSport;
import com.pennshape.app.adapter.PSSportArrayAdapter;
import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.request.PSDataUploadTaskRequest;
import com.pennshape.app.request.PSHttpTaskRequest;
import com.pennshape.app.request.PSUserDataTaskRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PSDailyUpdateFragmentTab extends Fragment implements PSHttpTaskRequest.PSHttpTaskRequestHandler{
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    ProgressDialog progress;

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
        //Submit
        final Button submit = (Button)view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        //Calendar
        ImageView calender = (ImageView)view.findViewById(R.id.calendar);
        calender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
    }

    private void submit(){
        String error = invalidate();
        if(error != null){
            displayError(error);
            return;
        }
        String dataString = populateDataString();
        PSDataUploadTaskRequest request = new PSDataUploadTaskRequest();
        request.setUserID(PSDataStore.getInstance().getUserID());
        request.setDataString(dataString);
        request.handler = this;
        request.execute();
        progress = ProgressDialog.show(getView().getContext(), "Loading...", "Please wait", true);
    }

    private void showCalendar(){
        Calendar today = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                getView().getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        TextView dateTextView = (TextView)getView().findViewById(R.id.date_text);
                        dateTextView.setText(dateFormat.format(newDate.getTime()));
                    }
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private String invalidate() {
        int editTextIDs[] = {R.id.edit1, R.id.edit2, R.id.edit3};
        for(int i=0;i<3;i++){
            String input = ((EditText)getView().findViewById(editTextIDs[i])).getText().toString();
            if(input==null || input.length()==0){
                return "Please enter the minutes you spent on each type of exercise";
            }
            Integer min = Integer.valueOf(input);
            if(min<0 || min>360) {
                return "The time for each type exercise should between 0' and 360'";
            }
        }
        return null;
    }

    private void displayError(String message) {
        new AlertDialog.Builder(getView().getContext())
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private String populateDataString(){
        //"{\"date\":\"2015-10-31\", \"c1\":\"10#A1****\", \"c2\":\"10#B2****\", \"c3\":\"10#C2C3**\"}";
        String dataString = "";
        //Spinners
        int spinnerIDs[] = {
                R.id.spinner11, R.id.spinner12, R.id.spinner13,
                R.id.spinner21, R.id.spinner22, R.id.spinner23,
                R.id.spinner31, R.id.spinner32, R.id.spinner33
        };
        //EditTexts
        int editTextIDs[] = {R.id.edit1, R.id.edit2, R.id.edit3};
        //keys
        String keys[] = {"c1", "c2", "c3"};

        JSONObject json = new JSONObject();
        String date = ((TextView)getView().findViewById(R.id.date_text)).getText().toString();
        try {
            json.put("date", date);
            for(int c=0; c<3; c++){
                String cString = ((EditText)getView().findViewById(editTextIDs[c])).getText().toString();
                cString += "#";
                for(int i=0; i<3; i++){
                    Spinner spinner = (Spinner)getView().findViewById(spinnerIDs[c*3+i]);
                    PSSport sport = (PSSport)spinner.getSelectedItem();
                    cString += sport.getSportDescription();
                }
                json.put(keys[c], cString);
            }
            dataString = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataString;
    }

    private void refreshUserData() {
        PSUserDataTaskRequest request = new PSUserDataTaskRequest();
        request.setUserID(PSDataStore.getInstance().getUserID());
        request.handler = this;
        request.execute();
    }

    @Override
    public void onSuccess(PSHttpTaskRequest request, Object result) {
        if(request instanceof PSDataUploadTaskRequest){
            refreshUserData();
        }else if(request instanceof PSUserDataTaskRequest){
            if(progress!=null) progress.dismiss();
            JSONObject userData = (JSONObject)result;
            try {
                PSDataStore.getInstance().reloadFronJson(userData);
            } catch (JSONException e) {
                displayError("User data parse failure");
                return;
            }
        }
    }

    @Override
    public void onFailure(PSHttpTaskRequest request, String error) {
        if(progress!=null) progress.dismiss();
        displayError(error);
    }
}
