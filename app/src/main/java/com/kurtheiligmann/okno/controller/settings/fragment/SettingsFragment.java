package com.kurtheiligmann.okno.controller.settings.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.kurtheiligmann.okno.R;
import com.kurtheiligmann.okno.controller.settings.adapter.MessageListAdapter;
import com.kurtheiligmann.okno.data.DataManager;
import com.kurtheiligmann.okno.data.Message;

import java.util.List;

public class SettingsFragment extends Fragment {

    private MessageListAdapter messageListAdapter;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        final DataManager dataManager = new DataManager(getActivity());

        final ListView messagesList = (ListView) rootView.findViewById(R.id.messages_list);
        registerForContextMenu(messagesList);


        CheckBox enabledCheckBox = (CheckBox) rootView.findViewById(R.id.enabled_checkbox);
        enabledCheckBox.setChecked(dataManager.getEnabled());
        enabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataManager.saveEnabled(isChecked);
                if (isChecked) {
                    populateMessageList(dataManager.getAllMessages(), messagesList);
                } else {
//                    populateMessageList(new ArrayList<Message>(), (ListView) rootView.findViewById(R.id.messages_list));
                }
            }
        });

        if (dataManager.getEnabled()) {
            populateMessageList(dataManager.getAllMessages(), (ListView) rootView.findViewById(R.id.messages_list));
        }

        return rootView;
    }

    private void populateMessageList(List<Message> messages, ListView messageList) {
        messageListAdapter = new MessageListAdapter(getActivity(), R.layout.message_list_item, messages.toArray(new Message[messages.size()]));
        messageList.setAdapter(messageListAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit_message:
                return true;
            case R.id.delete_message:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
