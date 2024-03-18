package com.example.lab2;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter implements Filterable {
    // get data to convert to view
    private ArrayList<Person> data;

    //Filter data
    private ArrayList<Person> dataBackup;

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
        // set the picture to the image view using library
        Uri imageUri = Uri.parse(data.get(position).getPicture());
        Glide.with(activity).load(imageUri).into(picture);

        // handle the checkbox click event
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    data.get(position).setCheck(isChecked);
            }
        });

        return v;
    }


    @Override
    public Filter getFilter() {
        Filter f = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults fr = new FilterResults();
                // Back up data: save the original data to dataBackup
                if (dataBackup == null) {
                    dataBackup = new ArrayList<>(data);
                }

                // If string to filter is null or empty, return the original data
                if (constraint == null || constraint.length() == 0) {
                    fr.count = dataBackup.size();
                    fr.values = dataBackup;
                }
                // If not null or empty, filter the data
                else {
                    ArrayList<Person> filteredData = new ArrayList<>();
                    for (Person p : dataBackup) {
                        if (p.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredData.add(p);
                        }
                    }
                    fr.count = filteredData.size();
                    fr.values = filteredData;
                }
                return fr;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data = new ArrayList<Person>();
                ArrayList<Person> tmp = (ArrayList<Person>) results.values;
                for (Person p : tmp) {
                    data.add(p);
                }
                notifyDataSetChanged();
            }
        };
        return f;
    }
}
