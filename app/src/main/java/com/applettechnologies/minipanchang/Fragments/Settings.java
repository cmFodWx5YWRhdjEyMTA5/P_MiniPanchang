package com.applettechnologies.minipanchang.Fragments;

import android.os.Bundle;

import com.applettechnologies.minipanchang.R;

import androidx.preference.PreferenceFragmentCompat;

public class Settings extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings,rootKey);
    }
}
