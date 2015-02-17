package com.kurtheiligmann.okno.controller.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kurtheiligmann.okno.R;
import com.kurtheiligmann.okno.data.Message;

import java.io.File;

/**
 * Created by kurtheiligmann on 2/16/15.
 */
public class MessageListAdapter extends ArrayAdapter<Message> {
    private final Context context;
    private final Message[] messages;

    public MessageListAdapter(Context context, int resource, Message[] messages) {
        super(context, resource, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Message message = messages[position];

        View rowView = convertView;

        if (rowView == null) {
            rowView = inflater.inflate(R.layout.message_list_item, parent, false);
        }

        TextView messageTextView = (TextView) rowView.findViewById(R.id.message_text);
        messageTextView.setText(message.getBody());

        File audioFile = new File(message.getAudioFilePath());
        TextView messageAudioTextView = (TextView) rowView.findViewById(R.id.message_audio);
        messageAudioTextView.setText(audioFile.getName());

        return rowView;
    }
}
