package com.kurtheiligmann.okno.controller.settings.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
    private TextView bodyText;
    private Button saveButton;
    private ListPopupWindow popupWindow;

    public EditMessageFragment() {
    }

    public static EditMessageFragment newInstance(Message message) {
        EditMessageFragment fragment = new EditMessageFragment();
        if (message != null) {
            Bundle args = new Bundle();
            args.putString(MESSAGE_ARG_KEY, message.getBody());
            fragment.setArguments(args);
        }
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

        MediaManager mediaManager = new MediaManager(getActivity());
        setTones(mediaManager.getAllTones());

        if (getArguments() != null) {

            String messageBody = getArguments().getString(MESSAGE_ARG_KEY);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_message, container, false);

        bodyText = (EditText) view.findViewById(R.id.message_body);
        bodyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toneText = (TextView) view.findViewById(R.id.message_tone);
        toneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

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

        saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                getMessage().setBody(bodyText.getText().toString());

                DataManager dataManager = new DataManager(getActivity());
                dataManager.saveMessage(getMessage());
                dataManager.close();

                getFragmentManager().popBackStack();
            }
        });

        if (getMessage() == null) {
            setMessage(Message.getInstance("", ""));
            saveButton.setEnabled(false);
        }

        Message message = getMessage();

        if (message != null) {
            if (message.getBody().length() > 0) {
                bodyText.setText(message.getBody());
            }

            if (message.getRingtoneName().length() > 0) {
                toneText.setText(message.getRingtoneName());
            }
        }

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        popupWindow.dismiss();

        OkNoTone selectedTone = getTones().get(position);
        String toneName = selectedTone.getName();

        if (getMessage() != null) {
            getMessage().setRingtoneName(toneName);
        }
        toneText.setText(toneName);

        validateFields();
    }

    private void validateFields() {
        saveButton.setEnabled(toneText.getText().length() > 0 && bodyText.getText().length() > 0);
    }

    private void hideSoftKeyboard(){
        if(getActivity().getCurrentFocus()!=null && getActivity().getCurrentFocus() instanceof EditText){
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(bodyText.getWindowToken(), 0);
        }
    }
}
