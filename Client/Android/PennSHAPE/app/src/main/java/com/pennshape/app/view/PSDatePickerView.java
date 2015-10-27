/**
 * Created by hasun on 10/20/15.
 */
package com.pennshape.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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
    private TextSwitcher switcher;
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
        setupControls();
        refreshDateRangeDisplay();

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
        if(switcher!=null) {
            if (byDay){
                switcher.setText(dateFormat.format(startDate.getTime()));
            }else {
                switcher.setText(dateFormat.format(startDate.getTime()) + " -- " + dateFormat.format(endDate.getTime()));
            }
        }
    }

    private void setupControls(){
        switcher = (TextSwitcher)findViewById(R.id.switcher);
        switcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView text = new TextView(getContext());
                text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                text.setTextAppearance(getContext(), R.style.UserDateFont);
                return text;
            }
        });
        switcher.setInAnimation(getContext(), android.R.anim.fade_in);
        switcher.setOutAnimation(getContext(), android.R.anim.fade_out);
        ImageButton leftArrow = (ImageButton)findViewById(R.id.left_arrow);
        ImageButton rightArrow = (ImageButton)findViewById(R.id.right_arrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
                switcher.setInAnimation(getContext(), android.R.anim.slide_in_left);
                switcher.setOutAnimation(getContext(), android.R.anim.slide_out_right);
                refreshDateRangeDisplay();
                if(mListener != null)   mListener.onDateChanged();
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
                switcher.setInAnimation(getContext(), R.anim.slide_in_right);
                switcher.setOutAnimation(getContext(), R.anim.slide_out_left);
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
