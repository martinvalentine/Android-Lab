package com.example.lab2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    private ArrayList<Contact> data;
    private LayoutInflater inflater;
    private Activity context;
    public void setData(ArrayList<Contact> data) {this.data = data;}
    public Adapter(ArrayList<Contact> data, Activity activity){
        this.data = data;
        this.context = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null)
            v = inflater.inflate(R.layout.activity_contact,null);
        EditText txtName = v.findViewById(R.id.edName);
        txtName.setText(data.get(position).getName());
        EditText txtPhone = v.findViewById(R.id.edPhone);
        txtPhone.setText(data.get(position).getPhone());
        CheckBox cb = v.findViewById(R.id.checkBox);
        cb.setChecked(data.get(position).getStatus());

        Contact c = data.get(position);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                c.setStatus(isChecked);
            }
        });
        return v;
    }
}

