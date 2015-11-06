package com.pennshape.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pennshape.app.R;
import com.pennshape.app.model.PSUser;
import com.squareup.picasso.Picasso;

/**
 * Created by hasun on 10/25/15.
 */
public class PSUserInfoSelectionView extends RelativeLayout {
    public interface PSUserInfoSelectionViewListener{
        void onSelectionChanged();
    }
    private ImageView imageView;
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
        imageView = (ImageView)findViewById(R.id.image);
        nameView = (TextView)findViewById(R.id.name);
        ageView = (TextView)findViewById(R.id.age);
        bmiView = (TextView)findViewById(R.id.bmi);
        favExecView = (TextView)findViewById(R.id.fav);
    }

    private void setupPhoto() {
        if (userModel.getID().equals("9001")){
            imageView.setImageResource(R.drawable.sample_photo_1);
        }else if (userModel.getID().equals("9002")){
            imageView.setImageResource(R.drawable.sample_photo_2);
        }else if (userModel.getID().equals("9003")){
            imageView.setImageResource(R.drawable.sample_photo_3);
        }else if (userModel.getID().equals("9004")){
            imageView.setImageResource(R.drawable.sample_photo_4);
        }else{
            Picasso.with(getContext())
                    .load(userModel.getPhoto())
                    .into(imageView);
        }
    }


    public void setUser(PSUser user){
        userModel = user;
        nameView.setText(userModel.getName());
        ageView.setText("("+userModel.getAgeDesc()+")");
        bmiView.setText("BMI: "+userModel.getBMIDesc());
        favExecView.setText("♥ Running");
        setupPhoto();
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
