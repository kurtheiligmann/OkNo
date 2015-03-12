package com.kurtheiligmann.okno.controller.settings.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.kurtheiligmann.okno.R;
import com.kurtheiligmann.okno.controller.settings.adapter.RingtoneListAdapter;
import com.kurtheiligmann.okno.data.DataManager;
import com.kurtheiligmann.okno.data.Message;
import com.kurtheiligmann.okno.data.OkNoTone;
import com.kurtheiligmann.okno.media.MediaManager;

import java.util.List;

public class EditMessageFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String MESSAGE_ARG_KEY = "MESSAGE_ARG_KEY";
    private static final String MESSAGE_RINGTONE_NAME_ARG_KEY = "MESSAGE_RINGTONE_NAME_ARG_KEY";

    private Message message;
    private List<OkNoTone> tones;

    private TextView toneText;
    private ListPopupWindow popupWindow;

    public EditMessageFragment() {
    }

    public static EditMessageFragment newInstance(Message message) {
        EditMessageFragment fragment = new EditMessageFragment();
        Bundle args = new Bundle();
        args.putString(MESSAGE_ARG_KEY, message.getBody());
        fragment.setArguments(args);
        return fragment;
    }

    private Message getMessage() {
        return message;
    }

    private void setMessage(Message message) {
        this.message = message;
    }

    private List<OkNoTone> getTones() {
        return tones;
    }

    private void setTones(List<OkNoTone> tones) {
        this.tones = tones;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MESSAGE_RINGTONE_NAME_ARG_KEY, getMessage().getRingtoneName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            String messageBody = getArguments().getString(MESSAGE_ARG_KEY);
            MediaManager mediaManager = new MediaManager(getActivity());
            if (messageBody != null && getMessage() == null) {
                DataManager dataManager = new DataManager(getActivity());
                setMessage(dataManager.getMessageWithBody(messageBody));
                dataManager.close();
            }

            if (savedInstanceState != null) {
                String ringtoneName = savedInstanceState.getString(MESSAGE_RINGTONE_NAME_ARG_KEY);
                if (ringtoneName != null) {
                    getMessage().setRingtoneName(ringtoneName);
                }
            }

            setTones(mediaManager.getAllTones());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_message, container, false);

        final EditText bodyText = (EditText) view.findViewById(R.id.message_body);

        toneText = (TextView) view.findViewById(R.id.message_tone);
        toneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow = new ListPopupWindow(getActivity());
                popupWindow.setAnchorView(toneText);
                popupWindow.setModal(true);
                popupWindow.setOnItemClickListener(EditMessageFragment.this);
                popupWindow.setWidth((int) (view.getWidth() * .90));
                popupWindow.setHeight((int) (view.getHeight() * .90));

                RingtoneListAdapter ringtoneAdapter = new RingtoneListAdapter(getActivity(), R.layout.ringtone_list_item, getTones());
                popupWindow.setAdapter(ringtoneAdapter);

                popupWindow.show();
            }
        });

        Button saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMessage().setBody(bodyText.getText().toString());

                DataManager dataManager = new DataManager(getActivity());
                dataManager.saveMessage(getMessage());
                dataManager.close();

                getFragmentManager().popBackStack();
            }
        });

        Message message = getMessage();

        if (message != null) {
            bodyText.setText(message.getBody());
            toneText.setText(message.getRingtoneName());
        }

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        popupWindow.dismiss();

        OkNoTone selectedTone = getTones().get(position);
        String toneName = selectedTone.getName();

        getMessage().setRingtoneName(toneName);
        toneText.setText(toneName);

    }
}
