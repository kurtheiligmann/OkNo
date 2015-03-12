package com.kurtheiligmann.okno.controller.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kurtheiligmann.okno.R;
import com.kurtheiligmann.okno.data.OkNoTone;

import java.util.List;

public class RingtoneListAdapter extends ArrayAdapter<OkNoTone> {

    public RingtoneListAdapter(Context context, int resource, List<OkNoTone> tones) {
        super(context, resource);
        addAll(tones);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        OkNoTone tone = getItem(position);

        View rowView = convertView;

        if (rowView == null) {
            rowView = inflater.inflate(R.layout.ringtone_list_item, parent, false);
        }

        TextView ringtoneTitleTextView = (TextView) rowView.findViewById(R.id.ringtone_title);
        ringtoneTitleTextView.setText(tone.getName());
        return rowView;
    }
}
