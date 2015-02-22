package com.kurtheiligmann.okno.controller.settings.adapter;

import android.content.Context;
import android.media.Ringtone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kurtheiligmann.okno.R;

import java.util.List;

/**
 * Created by kurtheiligmann on 2/16/15.
 */
public class RingtoneListAdapter extends ArrayAdapter<Ringtone> {
    private final Context context;
    private Ringtone selectedRingtone;

    public RingtoneListAdapter(Context context, int resource, Ringtone selectedRingtone, List<Ringtone> ringtones) {
        super(context, resource);
        this.context = context;
        this.selectedRingtone = selectedRingtone;
        this.addAll(ringtones);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Ringtone ringtone = getItem(position);

        View rowView = convertView;

        if (rowView == null) {
            rowView = inflater.inflate(R.layout.ringtone_list_item, parent, false);
        }

        TextView ringtoneTitleTextView = (TextView) rowView.findViewById(R.id.ringtone_title);
        ringtoneTitleTextView.setText(ringtone.getTitle(getContext()));

        CheckBox ringtoneSelectedCheckBox = (CheckBox) rowView.findViewById(R.id.ringtone_selected);
        ringtoneSelectedCheckBox.setChecked(ringtone == selectedRingtone);

        return rowView;
    }
}
