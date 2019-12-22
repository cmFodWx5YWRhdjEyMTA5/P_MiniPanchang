package com.applettechnologies.minipanchang.Helpers;

import android.location.Location;

import java.util.Locale;

public class LatLonPoint {
    private double lat;
    private double lon;

    public LatLonPoint(double latitude, double longitude) {
        this.lat = latitude;
        this.lon = longitude;
    }

    public LatLonPoint(Location loc) {
        this.lat = loc.getLatitude();
        this.lon = loc.getLongitude();
    }

    public double getLatitude() {
        return this.lat;
    }

    public double getLongitude() {
        return this.lon;
    }

    public String getShortText() {
        return "Lat: " + String.format(Locale.getDefault(), "%.3f", new Object[]{Double.valueOf(this.lat)}) + " Lon: " + String.format(Locale.getDefault(), "%.3f", new Object[]{Double.valueOf(this.lon)});
    }
}
