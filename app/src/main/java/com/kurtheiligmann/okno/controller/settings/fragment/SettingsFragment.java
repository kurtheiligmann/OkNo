package com.kurtheiligmann.okno.controller.settings.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kurtheiligmann.okno.R;
import com.kurtheiligmann.okno.data.Message;

import java.util.List;

public class SettingsFragment extends Fragment {
    private boolean enabled;
    private List<Message> messages;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        return rootView;
    }
}
