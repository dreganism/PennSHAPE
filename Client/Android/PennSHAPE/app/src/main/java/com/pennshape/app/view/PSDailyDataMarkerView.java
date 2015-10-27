/**
 * Created by hasun on 10/24/15.
 */
package com.pennshape.app.view;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.pennshape.app.R;
import com.pennshape.app.model.PSDailyData;


public class PSDailyDataMarkerView extends MarkerView {
    private TextView moderate;
    private TextView vigorous;
    private TextView muscle;
    private TextView steps;
    public PSDailyDataMarkerView (Context context, int layoutResource) {
        super(context, layoutResource);
        moderate = (TextView)findViewById(R.id.mod);
        vigorous = (TextView)findViewById(R.id.vig);
        muscle = (TextView)findViewById(R.id.mus);
        steps = (TextView)findViewById(R.id.stp);
    }
    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        PSDailyData data = (PSDailyData)entry.getData();
        if(data != null){
            moderate.setText("Moderate: " + data.getC1() +"'");
            vigorous.setText("Vigorous: " + data.getC2() +"'");
            muscle.setText("Muscle: " + data.getC3() +"'");
            steps.setText("Steps: " + data.getSteps());
        }else{
            moderate.setText(R.string.ps_sample_marker_mod);
            vigorous.setText(R.string.ps_sample_marker_vig);
            muscle.setText(R.string.ps_sample_marker_mus);
            steps.setText(R.string.ps_sample_marker_stp);
        }
    }

    @Override
    public int getXOffset() {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset() {
        return -getHeight()-5;
    }
}
