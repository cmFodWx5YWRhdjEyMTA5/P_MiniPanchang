package com.applettechnologies.minipanchang.Helpers;

import android.location.Location;
import android.util.Log;
import android.view.View;

import com.applettechnologies.minipanchang.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MiniTime {

    int hr;
    int min;
    //int sec;

    MiniTime(int hr, int min){
        this.hr=hr;
        this.min = min;
        //  this.sec = sec;
    }

    public int getHr() {
        return hr;
    }

    public int getMin() {
        return min;
    }
    /*
        public int getSec() {
            return sec;
        }
    */
    public void setHr(int hr) {
        this.hr = hr;
    }

    public void setMin(int min) {
        this.min = min;
    }
/*111
    public void setSec(int sec) {
        this.sec = sec;
    }*/

    public static MiniTime duobleToTime(double time){

        double min = (time%1)*60;
        //    double sec = (min%1)*60;

        MiniTime tm = new MiniTime((int) time,(int) min);

        return tm;
    }

    public static String toString(MiniTime tm){
        String s;

        if(tm.getHr()<10){
            s = "0"+tm.getHr()+":";
        } else {
            s = tm.getHr()+":";
        }

        String s2;
        if(tm.getMin()<10){
            Log.i("TIME",tm.getMin()+"");
            s2 = "0"+tm.getMin();
        }else{
            Log.i("TIME",tm.getMin()+"");
            s2 = tm.getMin()+"";
        }


        return s.concat(s2);
    }

    public static MiniTime addTime(MiniTime t1, MiniTime t2){

        //      int sec = t1.getSec()+t2.getSec();
        //      Log.i("addTime",""+"T1  "+t1.getSec()+"   T2   "+t2.getSec()+"  =  "+ sec);
        int min = t1.getMin()+t2.getMin();
        int hr = t1.getHr()+t2.getHr();
/*
        if(sec>60){
            int temp=sec;
            sec=sec%60;
            Log.i("addTime",""+"Seconds"+sec);
            temp=temp/60;
            min=min+temp;
        }*/
        if(min>60){
            int tmp = min;
            min=min%60;
            tmp=tmp/60;
            hr=tmp+hr;
        }
        MiniTime tm = new MiniTime(hr,min);
        Log.i("addTime",""+ MiniTime.toString(tm));
        return tm;
    }

    public static MiniTime twelthPart(double time){
        double temp=time;
        time = time/12;
        int hour = (int) time;
        int minute;
        if(time>=1){
            minute= (int) ((time-1)*60);
        }else{
            minute = (int) (time*60);
        }

        // int sec = (int) ((((time%1)*60)%1)*60)/1;

        return new MiniTime(hour,minute);
    }

    public static MiniTime eightPart(double time){
        double temp=time;
        time = time/8;
        int hour = (int) time;
        int minute = (int) ((time-1)*60);
        //    int sec = (int) ((((time - 1) * 60) % 1) * 60);

        Log.i("eightPArt",""+ MiniTime.toString(new MiniTime(hour,minute)));

        return new MiniTime(hour,minute);
    }
/*
    public static HashMap<String,MiniTime> Choghadiya(Calendar calendar, LatLonPoint mLocation, View view, Date date){

        HashMap<String,MiniTime> choghadiyaHashMap = new HashMap<String,MiniTime>();

        ArrayList<String> choghadiya = new ArrayList<String>(7);
        ArrayList<String> choghadiyaNameList = new ArrayList<String>(17);
        ArrayList<MiniTime> choghadiyaMiniTimeList = new ArrayList<MiniTime>(17);

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE");
        int start=0;

        String[] weekdays = view.getResources().getStringArray(R.array.weekdays);
        String[] choghadiya1 = view.getResources().getStringArray(R.array.choghadiya_list);
        for(int i=0;i<7;i++){
            if(simpleDateFormat.format(date).equals(weekdays[i])){
                start = i;
            }
            choghadiya.add(i,choghadiya1[i]);
            Log.i("Choghadiya Name",choghadiya1[i]);
        }
        //Todo: Populate choghadiyaNames
        choghadiyaNameList.add(0,choghadiya.get(start));
        int pos = start;
        for(int j=1;j<16;j++){
            if(j<8){
                pos = pos+5;
            }else{
                pos = pos+4;
            }
            if(pos>6){
                pos=pos-7;
                choghadiyaNameList.add(j,choghadiya.get(pos));
            }else{
                choghadiyaNameList.add(j,choghadiya.get(pos));
            }
        }
        choghadiyaNameList.add(16,"last");

        //Todo: Populate choghadiyaTime
        double rise = SunCalculator.sunrise(calendar, mLocation, SunCalculator.ZENITH_OFFICIAL);
        double set = SunCalculator.sunset(calendar, mLocation, SunCalculator.ZENITH_OFFICIAL);

        //Getting the eight part of DayLength
        MiniTime dayEightPart = MiniTime.eightPart(SunCalculator.dayLength(rise,set));

        choghadiyaMiniTimeList.add(0, MiniTime.duobleToTime(rise));
        MiniTime temp = MiniTime.duobleToTime(rise);
        for(int n=1;n<8;n++){
            MiniTime temp1 = MiniTime.addTime(temp,dayEightPart);
            choghadiyaMiniTimeList.add(n,temp1);
            Log.i("Choghadiya's",""+ MiniTime.toString(temp1));
            temp = temp1;
        }

        //Getting the eight part of DayLength
        MiniTime nightEightPart = MiniTime.eightPart(SunCalculator.dayLength(rise,set));

        choghadiyaMiniTimeList.add(8, MiniTime.duobleToTime(set));
        MiniTime tempNight = MiniTime.duobleToTime(set);
        for(int n=9;n<17;n++){
            MiniTime temp1 = MiniTime.addTime(tempNight,nightEightPart);
            choghadiyaMiniTimeList.add(n,temp1);
            Log.i("Choghadiya's",""+ MiniTime.toString(temp1));
            tempNight = temp1;
        }


        for(int m=0; m<8; m++){
            choghadiyaHashMap.put(choghadiyaNameList.get(m),choghadiyaMiniTimeList.get(m));
        }

        return choghadiyaHashMap;
    }
*/

}

