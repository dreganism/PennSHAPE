package com.pennshape.app.adapter;

import com.pennshape.app.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hasun on 10/30/15.
 */
public class PSSport {
    public String desc;
    public int imageID;
    public PSSport(){
        super();
    }
    public PSSport(int imageID, String desc){
        super();
        this.imageID = imageID;
        this.desc = desc;
    }

    public String getSportDescription(){
        if(imageID == R.drawable.sport_fw){
            return "A1";
        }else if(imageID == R.drawable.sport_jr){
            return "A2";
        }else if(imageID == R.drawable.sport_rb){
            return "A3";
        }else if(imageID == R.drawable.sport_sc){
            return "A4";
        }else if(imageID == R.drawable.sport_hk){
            return "A5";
        }else if(imageID == R.drawable.sport_dz){
            return "B1";
        }else if(imageID == R.drawable.sport_ru){
            return "B2";
        }else if(imageID == R.drawable.sport_mb){
            return "B3";
        }else if(imageID == R.drawable.sport_sw){
            return "B4";
        }else if(imageID == R.drawable.sport_tn){
            return "B5";
        }else if(imageID == R.drawable.sport_su){
            return "C1";
        }else if(imageID == R.drawable.sport_pu){
            return "C2";
        }else if(imageID == R.drawable.sport_sq){
            return "C3";
        }else if(imageID == R.drawable.sport_wl){
            return "C4";
        }else if(imageID == R.drawable.sport_yg){
            return "C5";
        }
        return "**";
    }

    static public int parseDrawableDescription(String desc) {
        if(desc.equals("A1")){
            return R.drawable.sport_fw;
        }else if(desc.equals("A2")){
            return R.drawable.sport_jr;
        }else if(desc.equals("A3")){
            return R.drawable.sport_rb;
        }else if(desc.equals("A4")){
            return R.drawable.sport_sc;
        }else if(desc.equals("A5")){
            return R.drawable.sport_hk;
        }else if(desc.equals("B1")){
            return R.drawable.sport_dz;
        }else if(desc.equals("B2")){
            return R.drawable.sport_ru;
        }else if(desc.equals("B3")){
            return R.drawable.sport_mb;
        }else if(desc.equals("B4")){
            return R.drawable.sport_sw;
        }else if(desc.equals("B5")){
            return R.drawable.sport_tn;
        }else if(desc.equals("C1")){
            return R.drawable.sport_su;
        }else if(desc.equals("C2")){
            return R.drawable.sport_pu;
        }else if(desc.equals("C3")){
            return R.drawable.sport_sq;
        }else if(desc.equals("C4")){
            return R.drawable.sport_wl;
        }else if(desc.equals("C5")){
            return R.drawable.sport_yg;
        }
        return -1;
    }

    static public ArrayList<Integer> parseDrawablesDescription(String desc) {
        ArrayList<Integer> sports = new ArrayList<>();
        int res = -1;
        if(desc.length()==6){
            for(int start=0;start<6;start+=2){
                res = parseDrawableDescription(desc.substring(start, start + 2));
                if(res>0 && !sports.contains(res)) {
                    sports.add(res);
                }
            }
        }
        return sports;
    }
}