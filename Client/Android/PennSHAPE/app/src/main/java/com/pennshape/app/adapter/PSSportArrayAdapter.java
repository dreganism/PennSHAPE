/**
 * Created by hasun on 10/30/15.
 */
package com.pennshape.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pennshape.app.R;

import java.util.ArrayList;
import java.util.List;

public class PSSportArrayAdapter extends ArrayAdapter<PSSport> {
    public enum SportCategory {
        Mod,/*Moderate*/
        Vig,/*Vigorous*/
        Mus /*Muscle*/
    }

    public PSSportArrayAdapter(Context context, int resource, SportCategory c) {
        super(context, resource);
        addAll(createItems(context, c));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, R.layout.ps_spinner_sport);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, R.layout.ps_spinner_sport_dropdonw);
    }

    public View getCustomView(int position, View convertView, int resource) {
        if(convertView == null){
            convertView = View.inflate(getContext(), resource, null);
        }
        ImageView image = (ImageView)convertView.findViewById(R.id.image);
        TextView text = (TextView)convertView.findViewById(R.id.description);
        image.setImageResource(getItem(position).imageID);
        if(text != null) {
            text.setText(getItem(position).desc);
        }
        return convertView;
    }

    private List<PSSport> createItems(Context context, SportCategory c){
        List<PSSport> sports = new ArrayList<>();
        switch (c) {
            case Mod:
                sports.add(new PSSport(R.drawable.sport_edit, context.getResources().getString(R.string.ps_sport_choose)));
                sports.add(new PSSport(R.drawable.sport_fw, context.getResources().getString(R.string.ps_sport_fast_walking)));
                sports.add(new PSSport(R.drawable.sport_jr, context.getResources().getString(R.string.ps_sport_jumping_ropes)));
                sports.add(new PSSport(R.drawable.sport_rb, context.getResources().getString(R.string.ps_sport_biking)));
                sports.add(new PSSport(R.drawable.sport_sc, context.getResources().getString(R.string.ps_sport_climbling_steps)));
                sports.add(new PSSport(R.drawable.sport_hk, context.getResources().getString(R.string.ps_sport_housekeeping)));
                break;
            case Vig:
                sports.add(new PSSport(R.drawable.sport_edit, context.getResources().getString(R.string.ps_sport_choose)));
                sports.add(new PSSport(R.drawable.sport_dz, context.getResources().getString(R.string.ps_sport_dancing)));
                sports.add(new PSSport(R.drawable.sport_ru, context.getResources().getString(R.string.ps_sport_running)));
                sports.add(new PSSport(R.drawable.sport_mb, context.getResources().getString(R.string.ps_sport_spinning)));
                sports.add(new PSSport(R.drawable.sport_sw, context.getResources().getString(R.string.ps_sport_swimming)));
                sports.add(new PSSport(R.drawable.sport_tn, context.getResources().getString(R.string.ps_sport_tennis)));
                break;
            case Mus:
                sports.add(new PSSport(R.drawable.sport_edit, context.getResources().getString(R.string.ps_sport_choose)));
                sports.add(new PSSport(R.drawable.sport_su, context.getResources().getString(R.string.ps_sport_sit_up)));
                sports.add(new PSSport(R.drawable.sport_pu, context.getResources().getString(R.string.ps_sport_push_up)));
                sports.add(new PSSport(R.drawable.sport_sq, context.getResources().getString(R.string.ps_sport_squat)));
                sports.add(new PSSport(R.drawable.sport_wl, context.getResources().getString(R.string.ps_sport_weightlifting)));
                sports.add(new PSSport(R.drawable.sport_yg, context.getResources().getString(R.string.ps_sport_yoga)));
                break;
            default: break;
        }
        return sports;
    }

}
