package com.pennshape.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pennshape.app.R;
import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.model.PSUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class PSSettingFragmentTab extends Fragment {


    public PSSettingFragmentTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ps_fragment_setting, container, false);
        setupViews(view);
        return view;
    }


    private void setupViews(View view) {
        PSUser user = PSDataStore.getInstance().getUser();
        ((TextView) view.findViewById(R.id.name)).setText(user.getName());
        //HARD CODED
        ((ImageView) view.findViewById(R.id.image)).setImageResource(R.drawable.sample_photo_1);
        ((TextView) view.findViewById(R.id.age)).setText("Age: " + user.getAgeDesc());
        ((TextView) view.findViewById(R.id.height)).setText("Height: "+user.getHeightDesc());
    }
}
