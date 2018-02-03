package com.example.edward.calforme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class ListFragment extends Fragment {

    ListView listview;
    CustomAdapter adapter;
    ArrayList<String> testArray;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listview = view.findViewById(R.id.listview);

        new Thread(new Runnable() {
            public void run() {
                testArray.add("lettuce");
                testArray.add("tomatoes");
                testArray.add("cucumbers");
                testArray.add("onions");
            }
        }).start();

        testArray = new ArrayList<>();
        adapter = new CustomAdapter(getActivity(), testArray);
        listview.setAdapter(adapter);

        return view;
    }
}
