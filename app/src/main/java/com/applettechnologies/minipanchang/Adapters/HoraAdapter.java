package com.applettechnologies.minipanchang.Adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.applettechnologies.minipanchang.Helpers.LatLonPoint;
import com.applettechnologies.minipanchang.Helpers.MiniTime;
import com.applettechnologies.minipanchang.Helpers.SunCalculator;
import com.applettechnologies.minipanchang.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HoraAdapter extends RecyclerView.Adapter<HoraAdapter.HoraViewHolder> {

    int NUM_OF_HORA=24;
    private static final long DURATION_IN_RIGHT_LEFT = 70;
    Boolean on_attach =true;

    //Outside
    Date date;
    Calendar calendar;

    ArrayList<String> hora = new ArrayList<String>(7);
    ArrayList<String> horaNameList = new ArrayList<String>(24);
    ArrayList<MiniTime> horaMiniTimeList = new ArrayList<MiniTime>(25);

    LatLonPoint mLocation ;

    Context mContext;

    public HoraAdapter(Calendar calendar,Date date,LatLonPoint mLocation, Context mContext){
        this.date = date;
        this.calendar = calendar;
        this.mLocation = mLocation;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HoraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_hora_item,parent,false);

        HoraViewHolder horaViewHolder = new HoraViewHolder(view);

        //Todo: populate hora
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE");

        Log.i("Hora Name",simpleDateFormat.format(date));
        int start=0;
        String[] weekdays = view.getResources().getStringArray(R.array.weekdays);
        String[] hora1 = view.getResources().getStringArray(R.array.hora_list);

        for(int i=0;i<7;i++){
            if(simpleDateFormat.format(date).equals(weekdays[i])){
                start = i;
            }
            hora.add(i,hora1[i]);
            Log.i("Hora Name",hora1[i]);
        }
        //Todo: populate horaNames
        horaNameList.add(0,hora.get(start));
        int pos = start;
        for(int j=1;j<24;j++){
            pos = pos+5;
            if(pos>6){
                pos=pos-7;
                horaNameList.add(j,hora.get(pos));
                Log.i("Hora Name",hora1[pos]);
            }else{
                horaNameList.add(j,hora.get(pos));
                Log.i("Hora Name",hora1[pos]);
            }
        }

        //Todo: populate horaTime
        double rise = SunCalculator.sunrise(calendar, mLocation, SunCalculator.ZENITH_OFFICIAL);
        double set = SunCalculator.sunset(calendar, mLocation, SunCalculator.ZENITH_OFFICIAL);

        //Getting the twelfth part of DayLength
        MiniTime dayTwelfthPart = MiniTime.twelthPart(SunCalculator.dayLength(rise,set));

        //Toast.makeText(getContext().getApplicationContext(),""+MiniTime.toString(dayTwelfthPart),Toast.LENGTH_LONG).show();

        horaMiniTimeList.add(0, MiniTime.duobleToTime(rise));
        MiniTime temp = MiniTime.duobleToTime(rise);
        for(int n=1;n<12;n++){
            MiniTime temp1 = MiniTime.addTime(temp,dayTwelfthPart);
            horaMiniTimeList.add(n,temp1);
            Log.i("hora's",""+ MiniTime.toString(temp1));
            temp = temp1;
        }

        //Getting the twelfth part of NightLength
        MiniTime nightTwelfthPart = MiniTime.twelthPart(24.0-SunCalculator.dayLength(rise,set));

        horaMiniTimeList.add(12, MiniTime.duobleToTime(set));
        MiniTime temp2 = MiniTime.duobleToTime(set);
        for(int n=13;n<25;n++){
            MiniTime temp3 = MiniTime.addTime(temp2,nightTwelfthPart);
            horaMiniTimeList.add(n,temp3);
            temp2 = temp3;
        }
        return horaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HoraViewHolder holder, int position) {
        holder.horaName.setText(horaNameList.get(position));
        holder.horaTimePeriod.setText(MiniTime.toString(horaMiniTimeList.get(position))+" - "+ MiniTime.toString(horaMiniTimeList.get(position+1)));
        holder.horaTimePeriod.setTextColor(mContext.getResources().getColor(R.color.dark_green));

        if(position<12){
            holder.timeSpecifier.setBackgroundResource(R.drawable.day);
        }else{
            holder.timeSpecifier.setBackgroundResource(R.drawable.night);
        }

        if(horaNameList.get(position).equals(hora.get(6))){
            holder.planetIcon.setBackgroundResource(R.drawable.sun6);
        }else if(horaNameList.get(position).equals(hora.get(4))){
            holder.planetIcon.setBackgroundResource(R.drawable.venus);
        }else if(horaNameList.get(position).equals(hora.get(2))){
            holder.planetIcon.setBackgroundResource(R.drawable.mercury);
        }else if(horaNameList.get(position).equals(hora.get(3))){
            holder.planetIcon.setBackgroundResource(R.drawable.jupiter);
        }else if(horaNameList.get(position).equals(hora.get(5))){
            holder.planetIcon.setBackgroundResource(R.drawable.saturn);
        }else if(horaNameList.get(position).equals(hora.get(0))){
            holder.planetIcon.setBackgroundResource(R.drawable.moon);
        }else if(horaNameList.get(position).equals(hora.get(1))){
            holder.planetIcon.setBackgroundResource(R.drawable.mars);
        }

        setAnimation(holder.itemView,on_attach?position:-1);
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
        view.setTranslationX(view.getX() + 50);
        view.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(view, "translationX", view.getX() + 50, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1.f);
        ObjectAnimator.ofFloat(view, "alpha", 0.f).start();
        animatorTranslateY.setStartDelay(not_first_item ? DURATION_IN_RIGHT_LEFT : (position * DURATION_IN_RIGHT_LEFT));
        animatorTranslateY.setDuration((not_first_item ? 2 : 1) * DURATION_IN_RIGHT_LEFT);
        animatorSet.playTogether(animatorTranslateY, animatorAlpha);
        animatorSet.start();
    }

    @Override
    public int getItemCount() {
        return NUM_OF_HORA;
    }

    public class HoraViewHolder extends RecyclerView.ViewHolder{

        ImageView planetIcon;
        TextView horaName;
        TextView horaTimePeriod;
        ImageView timeSpecifier;

        public HoraViewHolder(@NonNull View itemView) {
            super(itemView);

            planetIcon = itemView.findViewById(R.id.planetIcon);
            horaName = itemView.findViewById(R.id.horaName);
            horaTimePeriod = itemView.findViewById(R.id.horaTimePeriod);
            timeSpecifier = itemView.findViewById(R.id.horaTimeSpecifier);
        }

    }

}

