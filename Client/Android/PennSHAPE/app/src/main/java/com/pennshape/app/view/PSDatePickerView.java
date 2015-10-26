/**
 * Created by hasun on 10/20/15.
 */
package com.pennshape.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pennshape.app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PSDatePickerView extends RelativeLayout{
    public interface PSDatePickerViewListener {
        void onDateChanged();
    }
    private Calendar startDate;
    private Calendar endDate;
    private boolean byDay = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM.dd.yyyy", Locale.US);
    protected PSDatePickerViewListener mListener;

    public PSDatePickerView(Context context) {
        super(context);
        init();
    }

    public PSDatePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PSDatePickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ps_date_picker_view, this);
        resetDates();
        refreshDateRangeDisplay();
        setupControls();
    }

    private void resetDates(){
        Calendar now = Calendar.getInstance(Locale.US);
        if(byDay) {
            startDate = (Calendar)now.clone();
            endDate = (Calendar)now.clone();
        }else {
            int dayOfWeek = (now.get(Calendar.DAY_OF_WEEK) + 5) % 7;
            startDate = Calendar.getInstance(Locale.US);
            startDate.add(Calendar.DATE, -dayOfWeek);
            endDate = Calendar.getInstance(Locale.US);
            endDate.add(Calendar.DATE, 6 - dayOfWeek);
        }
    }

    private void refreshDateRangeDisplay() {
        TextView rangeDisplay = (TextView) findViewById(R.id.date_picker_display);
        if(rangeDisplay!=null) {
            if (byDay){
                rangeDisplay.setText(dateFormat.format(startDate.getTime()));
            }else {
                rangeDisplay.setText(dateFormat.format(startDate.getTime()) + " -- " + dateFormat.format(endDate.getTime()));
            }
        }
    }

    private void setupControls(){
        ImageButton leftArrow = (ImageButton)findViewById(R.id.left_arrow);
        ImageButton rightArrow = (ImageButton)findViewById(R.id.right_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
                refreshDateRangeDisplay();
                if(mListener != null)   mListener.onDateChanged();
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
                refreshDateRangeDisplay();
                if (mListener != null) mListener.onDateChanged();
            }
        });
    }

    private void previous(){
        if(byDay) {
            startDate.add(Calendar.DATE, -1);
            endDate.add(Calendar.DATE, -1);
        }else{
            startDate.add(Calendar.DATE, -7);
            endDate.add(Calendar.DATE, -7);
        }
    }

    private void next(){
        if(byDay) {
            startDate.add(Calendar.DATE, 1);
            endDate.add(Calendar.DATE, 1);
        }else{
            startDate.add(Calendar.DATE, 7);
            endDate.add(Calendar.DATE, 7);
        }
    }

    public Calendar getStartDate() { return startDate; }
    public Calendar getEndDate() { return  endDate; }
    public void setListener(PSDatePickerViewListener listener){ mListener = listener; }

    public void setByDay(boolean isByDay) {
        if(byDay!=isByDay) {
            byDay = isByDay;
            resetDates();
            refreshDateRangeDisplay();
            //notify the listener about the range change
            if (mListener != null) mListener.onDateChanged();
        }
    }
}
