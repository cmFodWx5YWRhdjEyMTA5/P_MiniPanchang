package com.applettechnologies.minipanchang.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import com.applettechnologies.minipanchang.BuildConfig;
import com.applettechnologies.minipanchang.Fragments.AboutUs;
import com.applettechnologies.minipanchang.Fragments.Choghadiya;
import com.applettechnologies.minipanchang.Fragments.Hora;
import com.applettechnologies.minipanchang.Fragments.LatLon;
import com.applettechnologies.minipanchang.Fragments.PrivacyPolicy;
import com.applettechnologies.minipanchang.Fragments.Settings;
import com.applettechnologies.minipanchang.Fragments.SettingsFragment;
import com.applettechnologies.minipanchang.Fragments.SunRiseSet;
import com.applettechnologies.minipanchang.Helpers.LatLonPoint;
import com.applettechnologies.minipanchang.Helpers.MiniTime;
import com.applettechnologies.minipanchang.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PrivacyPolicy.OnFragmentInteractionListener, AboutUs.OnFragmentInteractionListener, Choghadiya.OnFragmentInteractionListener, Hora.OnFragmentInteractionListener, LatLon.OnFragmentInteractionListener, SunRiseSet.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {

    private FirebaseAnalytics mFirebaseAnalytics;
    DrawerLayout drawer;

    static int adcount=0;
    Context context = this;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);

        MobileAds.initialize(context, "ca-app-pub-4521402201973005~3122467843");

        //Channel for Simple Notification
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel simpleChannel = new NotificationChannel("SIMPLE",getString(R.string.simple_channel_name), NotificationManager.IMPORTANCE_HIGH);
            simpleChannel.setDescription(getString(R.string.simple_channel_description));

            NotificationManager simpleNotificationManager = getSystemService(NotificationManager.class);
            simpleNotificationManager.createNotificationChannel(simpleChannel);

            /*
            //Channel for Simple Notification
            NotificationChannel choghadiyaChannel = new NotificationChannel("CHOGHADIYA",getString(R.string.choghadiya_channel_name), NotificationManager.IMPORTANCE_HIGH);
            choghadiyaChannel.setDescription(getString(R.string.choghadiya_channel_description));

            NotificationManager choghadiyaNotificationManager = getSystemService(NotificationManager.class);
            choghadiyaNotificationManager.createNotificationChannel(choghadiyaChannel);
       */ }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Replacing Fragment
        replaceContent(SunRiseSet.class);


        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("notify")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            replaceContent(AboutUs.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_language){
            String listItems[] = {"English","Hindi"};
            SharedPreferences sharedPreferences = getSharedPreferences("language",MODE_PRIVATE);
            String lang = sharedPreferences.getString("My_Lang","En");
            int ci =0;
            if(lang.equals("En")){
                ci =0;
            }else {
                ci = 1;
            }
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            mBuilder.setTitle("Change your language here");
            mBuilder.setSingleChoiceItems(listItems, ci, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0){
                        setLocacle("En");
                        recreate();
                    }else if(which==1){
                        setLocacle("hi");
                        recreate();
                    }
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = mBuilder.create();
            alertDialog.show();
        }else if (id == R.id.nav_sun_rise_set) {
            replaceContent(SunRiseSet.class);
        } else if (id == R.id.nav_choghadiya) {
            replaceContent(Choghadiya.class);
        } else if (id == R.id.nav_hora) {
            replaceContent(Hora.class);
        } else if (id == R.id.nav_location) {
            replaceContent(LatLon.class);
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Mini Panchang\n\n"+"https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mini Panchang");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }else if (id == R.id.nav_privacy) {
            replaceContent(PrivacyPolicy.class);
        }else if (id == R.id.nav_about_us) {
            replaceContent(AboutUs.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setLocacle(String lang){
        Locale locale =new Locale(lang);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("language",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences sharedPreferences = getSharedPreferences("language",MODE_PRIVATE);
        String lang = sharedPreferences.getString("My_Lang","En");
        Log.i("Shared Prefernece Lang",""+lang);
        setLocacle(lang);
    }

    public void replaceContent(Class classObject){

        drawer.closeDrawers();
        Fragment fragment= null;
        try {
            fragment = (Fragment) (classObject).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_from_right);
        fragmentTransaction.replace(R.id.content, fragment).commit();

        adcount = adcount+1;
        if((adcount%2) == 0){
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-4521402201973005/3525668815");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
