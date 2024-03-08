package com.example.lab2;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter {
    // get data to convert to view
    private ArrayList<Person> data;

    // create a layout inflater
    private LayoutInflater inflater;
    // Create an Activity
    private Activity activity;

    private TextView name;
    private TextView phoneNumber;
    private TextView email;
    private ImageView picture;
    private CheckBox checkBox;

    // Constructor
    public ContactAdapter(ArrayList<Person> data, Activity activity) {
        this.data = data;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE); // get the layout inflater to inflate the view to the adapter
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
        View v = convertView; // get the view to convert to the adapter view from the parent view
        if (v == null) {
            v = inflater.inflate(R.layout.activity_contactitem, null); // inflate the view to the adapter view
        }

        // initialize the views
        name = v.findViewById(R.id.contactName);
        phoneNumber = v.findViewById(R.id.contactPhoneNumber);
        email = v.findViewById(R.id.contactEmail);
        picture = v.findViewById(R.id.contactImage);
        checkBox = v.findViewById(R.id.contactCheckBox);

        // set the data to the views
        name.setText(data.get(position).getName());
        phoneNumber.setText(data.get(position).getPhoneNumber());
        email.setText(data.get(position).getEmail());
        picture.setImageURI(Uri.parse(data.get(position).getPicture()));
        checkBox.setChecked(data.get(position).isCheck());

        // handle the checkbox click event
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    data.get(position).setCheck(true);
                } else {
                    data.get(position).setCheck(false);
                }
            }
        });
        return v;


    }


}
