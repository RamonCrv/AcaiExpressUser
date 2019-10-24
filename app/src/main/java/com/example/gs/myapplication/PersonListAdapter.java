package com.example.gs.myapplication;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/14/2017.
 */

public class PersonListAdapter extends ArrayAdapter<Person> {

    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;
    // private int lastPosition = -1;

    /**
     * Default constructor for the PersonListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     */
    public PersonListAdapter(Context context, int resource, ArrayList<Person> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

}

























