package com.kurtheiligmann.okno.controller.settings.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.kurtheiligmann.okno.R;
import com.kurtheiligmann.okno.data.DataManager;
import com.kurtheiligmann.okno.data.Message;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        final DataManager dataManager = new DataManager(getActivity());
        CheckBox enabledCheckBox = (CheckBox)rootView.findViewById(R.id.enabled_checkbox);
        enabledCheckBox.setChecked(dataManager.getEnabled());
        enabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataManager.saveEnabled(isChecked);
                if (isChecked) {
                    populateMessageList(dataManager.getAllMessages());
                } else {
                    populateMessageList(new ArrayList<Message>());
                }
            }
        });

        return rootView;
    }

    private void populateMessageList(List<Message> messages) {

    }
}
