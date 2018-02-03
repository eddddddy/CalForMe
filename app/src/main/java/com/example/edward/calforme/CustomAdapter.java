package com.example.edward.calforme;

import android.app.Activity;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

public class CustomAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<String> itemArray;

    public CustomAdapter(Activity context, ArrayList<String> itemArrayp) {
        super(context, R.layout.listview_row, itemArrayp);

        this.context = context;
        this.itemArray = itemArrayp;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row,null);

        CheckBox checkBox = rowView.findViewById(R.id.checkBox);
        checkBox.setText(itemArray.get(position));

        return rowView;
    }

}
