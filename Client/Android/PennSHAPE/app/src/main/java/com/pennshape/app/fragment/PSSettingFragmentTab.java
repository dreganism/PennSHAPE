package com.pennshape.app.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.pennshape.app.R;
import com.pennshape.app.activity.PSLoginActivity;
import com.pennshape.app.activity.PSMainActivity;
import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.model.PSUser;
import com.squareup.picasso.Picasso;

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
        setupControls(view);
        return view;
    }


    private void setupViews(View view) {
        PSUser user = PSDataStore.getInstance().getUser();
        ((TextView) view.findViewById(R.id.name)).setText(user.getName());
        Picasso.with(view.getContext())
                .load(user.getPhoto())
                .into((ImageView) view.findViewById(R.id.image));
        ((TextView) view.findViewById(R.id.age)).setText("Age: " + user.getAgeDesc());
        ((TextView) view.findViewById(R.id.height)).setText("Height: " + user.getHeightDesc());
        ((TextView) view.findViewById(R.id.weight)).setText("Weight: " + user.getWeightDesc());
    }

    private void setupControls(final View view) {
        Button logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout(){
        //reset ID
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.ps_pref_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(getString(R.string.ps_pref_key_id));
        editor.apply();
        //Switch to login
        Intent intent;
        intent = new Intent(getActivity(), PSLoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
