package com.applettechnologies.minipanchang.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.applettechnologies.minipanchang.Helpers.LatLonPoint;
import com.applettechnologies.minipanchang.Helpers.MiniTime;
import com.applettechnologies.minipanchang.Helpers.SunCalculator;
import com.applettechnologies.minipanchang.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SunRiseSet.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SunRiseSet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SunRiseSet extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private AdView mAdView;

    TextView cal;
    TextView sunrise;
    TextView sunset;
    TextView dayLength;
    TextView nightLength;
    CardView dateButton;
    ImageView imageView;

    public SunRiseSet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SunRiseSet.
     */
    // TODO: Rename and change types and number of parameters
    public static SunRiseSet newInstance(String param1, String param2) {
        SunRiseSet fragment = new SunRiseSet();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sun_rise_set, container, false);

        dateButton = (CardView) view.findViewById(R.id.date_button);
        cal = (TextView) view.findViewById(R.id.cal);
        sunrise = (TextView) view.findViewById(R.id.sunrise);
        sunset = (TextView) view.findViewById(R.id.sunset);
        dayLength = (TextView) view.findViewById(R.id.day_length);
        nightLength = (TextView) view.findViewById(R.id.night_length);

        //Requesting in app Permission from android device
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity().getApplicationContext(), "Please grant permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        LocationManager locationManager;
        double lat = 0.0;
        double lng = 0.0;

        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

            //Setting last known location
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location == null){

            }else{
                lat = location.getLatitude();
                lng = location.getLongitude();
            }


        }
        catch(SecurityException e) {
            e.printStackTrace();
        }


        final LatLonPoint mLocation = new LatLonPoint(lat, lng);
        final Calendar calendar = Calendar.getInstance();
        setScreenText(calendar,mLocation);


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        setScreenText(calendar,mLocation);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        mAdView = view.findViewById(R.id.adViewSun);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Inflate the layout for this fragment
        return view;
    }

    public void setScreenText(Calendar calendar,LatLonPoint mLocation){
        double rise = SunCalculator.sunrise(calendar, mLocation, SunCalculator.ZENITH_OFFICIAL);
        double set = SunCalculator.sunset(calendar, mLocation, SunCalculator.ZENITH_OFFICIAL);

        double DMin = (SunCalculator.dayLength(rise,set)%1)*60;
        double DSec = (DMin%1)*60;

        double NMin = ((24.0- SunCalculator.dayLength(rise,set))%1)*60;
        double NSec = (DMin%1)*60;

        MiniTime dawn = MiniTime.duobleToTime(rise);
        MiniTime dusk = MiniTime.duobleToTime(set);

        cal.setText(""+calendar.getTime());
        sunrise.setText(""+ MiniTime.toString(dawn));
        sunset.setText(""+ MiniTime.toString(dusk));
        dayLength.setText(""+ MiniTime.toString(MiniTime.duobleToTime(SunCalculator.dayLength(rise,set))));
        nightLength.setText(""+ MiniTime.toString(MiniTime.duobleToTime(24.0-SunCalculator.dayLength(rise,set))));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
