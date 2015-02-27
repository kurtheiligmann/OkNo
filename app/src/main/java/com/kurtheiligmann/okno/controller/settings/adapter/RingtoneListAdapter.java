package com.kurtheiligmann.okno.controller.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kurtheiligmann.okno.R;
import com.kurtheiligmann.okno.data.Tone;

import java.util.List;

/**
 * Created by kurtheiligmann on 2/16/15.
 */
public class RingtoneListAdapter extends ArrayAdapter<Tone> {
    private final Context context;
    private Tone selectedTone;

    public RingtoneListAdapter(Context context, int resource, Tone tone, List<Tone> tones) {
        super(context, resource);
        this.context = context;
        this.selectedTone = tone;
        this.addAll(tones);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Tone tone = getItem(position);

        View rowView = convertView;

        if (rowView == null) {
            rowView = inflater.inflate(R.layout.ringtone_list_item, parent, false);
        }

        TextView ringtoneTitleTextView = (TextView) rowView.findViewById(R.id.ringtone_title);
        ringtoneTitleTextView.setText(tone.getTitle());

        CheckBox ringtoneSelectedCheckBox = (CheckBox) rowView.findViewById(R.id.ringtone_selected);
        ringtoneSelectedCheckBox.setChecked(tone == selectedTone);

        return rowView;
    }
}
