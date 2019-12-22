package com.applettechnologies.minipanchang.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.applettechnologies.minipanchang.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LatLon.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LatLon#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LatLon extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private AdView mAdView;

    TextView latInDecimal;
    TextView latInDegree;
    TextView lngInDecimal;
    TextView lngInDegree;
    TextView signalAccuracy;
    Double lat=0.0;
    Double lng=0.0;

    LocationManager locationManager;


    public LatLon() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LatLon.
     */
    // TODO: Rename and change types and number of parameters
    public static LatLon newInstance(String param1, String param2) {
        LatLon fragment = new LatLon();
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
        View view = inflater.inflate(R.layout.fragment_lat_lon, container, false);

        mAdView = view.findViewById(R.id.adViewLocation);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        latInDecimal = (TextView) view.findViewById(R.id.latInDecimal);
        lngInDecimal = (TextView) view.findViewById(R.id.lngInDecimal);

        latInDegree = (TextView) view.findViewById(R.id.latInDegree);
        lngInDegree = (TextView) view.findViewById(R.id.lngInDegree);

        signalAccuracy = (TextView) view.findViewById(R.id.signnalAccuracy);

        //Requesting in app Permission from android device
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity().getApplicationContext(), "Please grant permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }


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

            latInDecimal.setText(""+lat);
            lngInDecimal.setText(""+lng);

            latInDegree.setText(Location.convert(lat,Location.FORMAT_SECONDS));
            lngInDegree.setText(Location.convert(lng,Location.FORMAT_SECONDS));

            if(location != null){
                signalAccuracy.setText(""+location.getAccuracy());
            }

        }
        catch(SecurityException e) {
            e.printStackTrace();
        }

        // Inflate the layout for this fragment
        return view;
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
        lat = location.getLatitude();
        lng = location.getLongitude();

        latInDecimal.setText(""+lat);
        lngInDecimal.setText(""+lng);

        latInDegree.setText(Location.convert(lat,Location.FORMAT_SECONDS));
        lngInDegree.setText(Location.convert(lng,Location.FORMAT_SECONDS));

        signalAccuracy.setText(""+location.getAccuracy());
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
