package com.applettechnologies.minipanchang.Adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.applettechnologies.minipanchang.Helpers.DatabaseHelper;
import com.applettechnologies.minipanchang.Helpers.LatLonPoint;
import com.applettechnologies.minipanchang.Helpers.MiniTime;
import com.applettechnologies.minipanchang.Helpers.SunCalculator;
import com.applettechnologies.minipanchang.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChoghadiyaAdapter extends RecyclerView.Adapter<ChoghadiyaAdapter.ChoghadiyaViewHolder> {

    int NUM_OF_CHOGHADIYA=16;
    private static final long DURATION_IN_RIGHT_LEFT = 70;
    Boolean on_attach = true;

    //Outside
    Date date;
    Calendar calendar;

    //Inside
    DatabaseHelper myDbHelper;
    Cursor c = null;

    ArrayList <String>  choghadiya = new ArrayList<String>(7);
    ArrayList<String>   choghadiyaNameList = new ArrayList<String>(17);
    ArrayList<MiniTime> choghadiyaMiniTimeList = new ArrayList<MiniTime>(17);

    LatLonPoint mLocation;

    HashMap<String,MiniTime> choghadiyaHashMap;

    Context mContext;

    public ChoghadiyaAdapter(Calendar calendar,Date date,LatLonPoint mLocation, Context mContext){
        this.date = date;
        this.calendar = calendar;
        this.mLocation = mLocation;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChoghadiyaAdapter.ChoghadiyaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.recyclerview_choghadiya_item,parent,false);
        ChoghadiyaViewHolder choghadiyaViewHolder =new ChoghadiyaViewHolder(view);

        //Todo: Populate choghadiya
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

        return choghadiyaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChoghadiyaAdapter.ChoghadiyaViewHolder holder, int position) {
        holder.choghadiyaName.setText(choghadiyaNameList.get(position));
        holder.choghadiyaTimePeriod.setText(MiniTime.toString(choghadiyaMiniTimeList.get(position))+" - "+ MiniTime.toString(choghadiyaMiniTimeList.get(position+1)));

        if(position<8){
            holder.timeSpecifier.setBackgroundResource(R.drawable.day);
        }else{
            holder.timeSpecifier.setBackgroundResource(R.drawable.night);
        }


        if(choghadiyaNameList.get(position).equals(choghadiya.get(4))||choghadiyaNameList.get(position).equals(choghadiya.get(3))||choghadiyaNameList.get(position).equals(choghadiya.get(2))||choghadiyaNameList.get(position).equals(choghadiya.get(0))){
            holder.choghadiyaName.setTextColor(mContext.getResources().getColor(R.color.dark_green));
            holder.choghadiyaTimePeriod.setTextColor(mContext.getResources().getColor(R.color.dark_green));
            holder.choghadiyaFal.setText("Auspicious");
            holder.choghadiyaFal.setTextColor(mContext.getResources().getColor(R.color.dark_green));
        }else{
            holder.choghadiyaName.setTextColor(Color.RED);
            holder.choghadiyaTimePeriod.setTextColor(Color.RED);
            holder.choghadiyaFal.setText("Inauspicious");
            holder.choghadiyaFal.setTextColor(Color.RED);
        }


        setAnimation(holder.itemView, on_attach?position:-1);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void setAnimation(View view, int position){
        boolean not_first_item = position == -1;
        position = position + 1;
        view.setTranslationX(view.getX() + 25);
        view.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(view, "translationX", view.getX() + 25, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1.f);
        ObjectAnimator.ofFloat(view, "alpha", 0.f).start();
        animatorTranslateY.setStartDelay(not_first_item ? DURATION_IN_RIGHT_LEFT : (position * DURATION_IN_RIGHT_LEFT));
        animatorTranslateY.setDuration((not_first_item ? 2 : 1) * DURATION_IN_RIGHT_LEFT);
        animatorSet.playTogether(animatorTranslateY, animatorAlpha);
        animatorSet.start();
    }

    @Override
    public int getItemCount() {
        return NUM_OF_CHOGHADIYA;
    }

    public class ChoghadiyaViewHolder extends RecyclerView.ViewHolder{

        TextView choghadiyaName;
        TextView choghadiyaTimePeriod;
        TextView choghadiyaFal;
        ImageView timeSpecifier;

        public ChoghadiyaViewHolder(@NonNull View itemView) {
            super(itemView);

            choghadiyaName = itemView.findViewById(R.id.choghadiyaName);
            choghadiyaTimePeriod = itemView.findViewById(R.id.choghadiyaTimePeriod);
            choghadiyaFal = itemView.findViewById(R.id.choghadiyaFal);
            timeSpecifier = itemView.findViewById(R.id.choghadiyaTimeSpecifier);
        }

    }
}
