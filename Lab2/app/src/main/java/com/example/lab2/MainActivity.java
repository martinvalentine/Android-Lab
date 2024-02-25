package com.example.lab2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private EditText edName;
    private EditText edPhone;
    private CheckBox checkBox;
    private Button btnThem;
    private Button btnXoa;
    private ListView lstContact;
    private ArrayList<Contact> ContactList;
    protected Adapter ListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edName = findViewById(R.id.txtName);
//        edPhone = findViewById(R.id.edPhone);
//        checkBox = findViewById(R.id.checkBox);
        btnThem = findViewById(R.id.btnThem);
        btnXoa = findViewById(R.id.btnXoa);
        lstContact = findViewById(R.id.lstContact);

        ContactList = new ArrayList<Contact>();
        ContactList.add(new Contact(1, "Nguyễn Văn A","0913023232", false,"Image"));
        ContactList.add(new Contact(2, "Lê A","0913023232", false,"Image"));
        ListAdapter = new Adapter(ContactList,this);
        lstContact.setAdapter(ListAdapter);

        ArrayList<Integer> list = new ArrayList<Integer>();;
        lstContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edName.setText(ListAdapter.getItem(position).toString());
                //ListAdapter.getItemId(position);
//                for(Contact x : ContactList){
//                    if(x.getId() == ListAdapter.getItemId(position)){
//
//                    }
//                }
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle("Xác nhận");
                b.setMessage("Bạn có chắc chắn muốn xoá không?");
                b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Iterator<Contact> iterator = ContactList.iterator();
                        while (iterator.hasNext()) {
                            Contact x = iterator.next();
                            if (x.getStatus()) {
                                iterator.remove();
                            }
                        }
                        ListAdapter = new Adapter(ContactList,MainActivity.this);
                        lstContact.setAdapter(ListAdapter);
                    }
                });
                b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog al = b.create();
                al.show();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Them.class);
                startActivityForResult(intent,100);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle b = data.getExtras();
        int id = b.getInt("Id");
        String name = b.getString("Name");
        String phone = b.getString("Phone");
        String image = b.getString("Image");
            Contact newcontact = new Contact(id, name,phone, false,image);
            if(requestCode == 100 && resultCode ==150){
                //truong hop them
                ContactList.add(newcontact);
                ListAdapter.notifyDataSetChanged();
            }



    }


    public boolean validateId(ArrayList<Contact> c, int id){
        boolean res = true;
        for(Contact x : c){
            if(x.getId() == id){
                res = false;

            }
        }
        return res;
    }

}