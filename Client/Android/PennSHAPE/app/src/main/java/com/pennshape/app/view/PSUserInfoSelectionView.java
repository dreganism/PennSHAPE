package com.pennshape.app.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
    private int userColor;
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

    public void setUserColor(int color) {
        setBackgroundColor(adjustAlpha(color, 0.8f));
        userColor = color;
    }

    public int getUserColor(){
        return userColor;
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
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
        //Image Dialog
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.ps_photo_dialog);
                dialog.setTitle(userModel.getName());
                ImageView photoView = (ImageView)dialog.findViewById(R.id.image);
                Picasso.with(getContext())
                        .load(userModel.getPhoto())
                        .into(photoView);
                dialog.show();
            }
        });
    }


    public void setUser(PSUser user){
        userModel = user;
        nameView.setText(userModel.getName());
        ageView.setText("("+userModel.getAgeDesc()+")");
        bmiView.setText("BMI: "+userModel.getBMIDesc());
        favExecView.setText(getContext().getResources().getText(R.string.ps_sample_fav_exer));
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
