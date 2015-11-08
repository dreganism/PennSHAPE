/**
 * Created by hasun on 10/24/15.
 */
package com.pennshape.app.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.pennshape.app.R;
import com.pennshape.app.adapter.PSSport;
import com.pennshape.app.model.PSDailyData;

import java.util.ArrayList;


public class PSDailyDataMarkerView extends MarkerView {
    private TextView moderate;
    private TextView vigorous;
    private TextView muscle;
    private TextView steps;
    private ImageView[][] images = new ImageView[3][3];
    private int curIndex = 0;
    public PSDailyDataMarkerView (Context context, int layoutResource) {
        super(context, layoutResource);
        moderate = (TextView)findViewById(R.id.mod);
        vigorous = (TextView)findViewById(R.id.vig);
        muscle = (TextView)findViewById(R.id.mus);
        steps = (TextView)findViewById(R.id.stp);
        images[0][0] = (ImageView)findViewById(R.id.img_mod_1);
        images[0][1] = (ImageView)findViewById(R.id.img_mod_2);
        images[0][2] = (ImageView)findViewById(R.id.img_mod_3);
        images[1][0] = (ImageView)findViewById(R.id.img_vig_1);
        images[1][1] = (ImageView)findViewById(R.id.img_vig_2);
        images[1][2] = (ImageView)findViewById(R.id.img_vig_3);
        images[2][0] = (ImageView)findViewById(R.id.img_mus_1);
        images[2][1] = (ImageView)findViewById(R.id.img_mus_2);
        images[2][2] = (ImageView)findViewById(R.id.img_mus_3);
    }
    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        curIndex = entry.getXIndex();
        PSDailyData data = (PSDailyData)entry.getData();
        if(data != null){
            moderate.setText("Moderate: " + data.getC1() +"'");
            vigorous.setText("Vigorous: " + data.getC2() +"'");
            muscle.setText("Muscle: " + data.getC3() +"'");
            steps.setText("Steps: " + data.getSteps());
            refreshImages(data);
        }else{
            moderate.setText(R.string.ps_sample_marker_mod);
            vigorous.setText(R.string.ps_sample_marker_vig);
            muscle.setText(R.string.ps_sample_marker_mus);
            steps.setText(R.string.ps_sample_marker_stp);
        }
    }

    @Override
    public int getXOffset() {
        if(curIndex==0) return 0;
        if(curIndex==1) return -(getWidth() / 4);
        if(curIndex==5) return -(getWidth()*3 / 4);
        if(curIndex==6) return -getWidth();
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset() {
        return -getHeight()-23;
    }

    private void refreshImages(PSDailyData data) {
        String [] descriptions = {data.getD1(), data.getD2(), data.getD3()};
        for(int i=0;i<3;i++){
            ArrayList<Integer> drawables = PSSport.parseDrawablesDescription(descriptions[i]);
            for(int j=0;j<3;j++){
                if(j<drawables.size()){
                    images[i][j].setImageResource(drawables.get(j));
                }else{
                    images[i][j].setImageResource(0);
                }
            }
        }
    }
}
