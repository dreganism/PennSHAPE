/**
 * Created by hasun on 10/24/15.
 */
package com.pennshape.app.view;

import android.content.Context;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;


public class PSDailyDataMarkerView extends MarkerView {
    public PSDailyDataMarkerView (Context context, int layoutResource) {
        super(context, layoutResource);
    }
    @Override
    public void refreshContent(Entry entry, Highlight highlight) {

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
