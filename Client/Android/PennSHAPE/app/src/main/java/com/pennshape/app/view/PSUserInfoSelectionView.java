package com.pennshape.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pennshape.app.R;
import com.pennshape.app.model.PSUser;

/**
 * Created by hasun on 10/25/15.
 */
public class PSUserInfoSelectionView extends RelativeLayout {
    public interface PSUserInfoSelectionViewListener{
        void onSelectionChanged();
    }
    private CheckBox checkBox;
    private TextView nameView;
    private TextView ageView;
    private TextView bmiView;
    private TextView favExecView;
    private PSUser userModel;
    public PSUserInfoSelectionViewListener mListener;
    public PSUserInfoSelectionView(Context context) {
        super(context);
        init();
    }
    public PSUserInfoSelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PSUserInfoSelectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ps_user_info_selection_view, this);
        checkBox = (CheckBox)findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mListener!=null){
                    mListener.onSelectionChanged();
                }
            }
        });
        nameView = (TextView)findViewById(R.id.name);
        ageView = (TextView)findViewById(R.id.age);
        bmiView = (TextView)findViewById(R.id.bmi);
        favExecView = (TextView)findViewById(R.id.fav);
    }

    public void setUser(PSUser user){
        userModel = user;
        nameView.setText(userModel.getName());
        ageView.setText("("+userModel.getAgeDesc()+")");
        bmiView.setText("BMI: "+userModel.getBMIDesc());
        favExecView.setText("♥ Running");
    }

    public PSUser selectedUser(){
        if(checkBox != null){
            if(checkBox.isChecked()){
                return userModel;
            }
        }
        return null;
    }

}
