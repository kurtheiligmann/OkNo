package com.kurtheiligmann.okno.controller.settings.fragment;

import android.app.Fragment;
import android.media.Ringtone;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kurtheiligmann.okno.R;
import com.kurtheiligmann.okno.data.DataManager;
import com.kurtheiligmann.okno.data.Message;
import com.kurtheiligmann.okno.media.MediaManager;

import java.util.List;

public class EditMessageFragment extends Fragment {

    public static final String FRAGMENT_NAME = "EditMessageFragment";
    public static final String MESSAGE_ARG_KEY = "MESSAGE_ARG_KEY";

    private Message message;

    public static EditMessageFragment newInstance(Message message) {
        EditMessageFragment fragment = new EditMessageFragment();
        Bundle args = new Bundle();
        args.putString(MESSAGE_ARG_KEY, message.getBody());
        fragment.setArguments(args);
        return fragment;
    }

    public EditMessageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            DataManager dataManager = new DataManager(getActivity());
            String messageBody = getArguments().getString(MESSAGE_ARG_KEY);
            message = dataManager.getMessageWithBody(messageBody);

            MediaManager mediaManager = new MediaManager(getActivity());
            List<Ringtone> ringtones = mediaManager.getAllRingtones();
            for (Ringtone ringtone : ringtones) {
                Log.i(this.getClass().toString(), "ringtone: " + ringtone.getTitle(getActivity()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_message, container, false);

        TextView bodyText = (TextView) view.findViewById(R.id.message_body);
        bodyText.setText(message.getBody());

        TextView toneText = (TextView) view.findViewById(R.id.message_tone);
        toneText.setText(message.getAudioFileName());

        return view;
    }
}
