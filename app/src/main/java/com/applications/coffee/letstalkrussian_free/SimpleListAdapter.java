package com.applications.coffee.letstalkrussian_free;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by essabri on 19/03/2018.
 */

public class SimpleListAdapter extends BaseAdapter {

    private String[] data;
    private Context c;

    public SimpleListAdapter(Context context, String[] str) {
        c = context;
        data = str;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(c);
            convertView = inflater.inflate(R.layout.simple_list_item,parent,false);
        }

        ((TextView)convertView.findViewById(R.id.phraseToCopy)).setText(data[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
