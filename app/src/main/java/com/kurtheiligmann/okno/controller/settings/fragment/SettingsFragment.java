package com.kurtheiligmann.okno.controller.settings.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
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
    private Button newMessageButton;
    private View rootView;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        final DataManager dataManager = new DataManager(getActivity());

        final ListView messagesList = (ListView) rootView.findViewById(R.id.messages_list);
        registerForContextMenu(messagesList);
        messagesList.setOverScrollMode(AbsListView.OVER_SCROLL_ALWAYS);
        messagesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            int previousFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > previousFirstVisibleItem) {
                    hideAddButton();
                } else {
                    showAddButton();
                }
            }
        });

        CheckBox enabledCheckBox = (CheckBox) rootView.findViewById(R.id.enabled_checkbox);
        enabledCheckBox.setChecked(dataManager.getEnabled());
        enabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataManager.saveEnabled(isChecked);
            }
        });

        newMessageButton = (Button) rootView.findViewById(R.id.new_message_button);
        newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewMessageView();
            }
        });

        if (dataManager.getEnabled()) {
            populateMessageList(dataManager.getAllMessages(), (ListView) rootView.findViewById(R.id.messages_list));
        }

        dataManager.close();
        return rootView;
    }

    private void hideAddButton() {
        if (newMessageButton != null) {
            newMessageButton.animate().y(rootView.getHeight());
        }
    }

    private void showAddButton() {
        if (newMessageButton != null) {
            newMessageButton.animate().y(rootView.getHeight() - newMessageButton.getHeight());
        }
    }

    private void showNewMessageView() {
        showEditMessage(null);
    }

    private void populateMessageList(List<Message> messages, ListView messageList) {
        messageListAdapter = new MessageListAdapter(getActivity(), R.layout.message_list_item, messages);
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
        DataManager dataManager = new DataManager(getActivity());
        Message selectedMessage = dataManager.getAllMessages().get(info.position);
        boolean handled = false;
        switch (item.getItemId()) {
            case R.id.edit_message:
                showEditMessage(selectedMessage);
                handled = true;
                break;
            case R.id.delete_message:
                dataManager.deleteMessage(selectedMessage);
                messageListAdapter.swapMessages(dataManager.getAllMessages());
                handled = true;
                break;
            default:
                handled = super.onContextItemSelected(item);
                break;
        }

        dataManager.close();

        return handled;
    }

    private void showEditMessage(Message message) {
        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.container, EditMessageFragment.newInstance(message));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
