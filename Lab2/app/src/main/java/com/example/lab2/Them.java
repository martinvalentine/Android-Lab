package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Them extends AppCompatActivity {

    private EditText edId;
    private EditText edName;
    private EditText edPhone;
    private ImageView imageView;
    private Button btnAdd;
    private Button btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them);

        edId = findViewById(R.id.edId);
        edPhone = findViewById(R.id.edPhone);
        edName = findViewById(R.id.edName);
        imageView = findViewById(R.id.imageView);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(edId.getText().toString());
                String name = edName.getText().toString();
                String phone = edPhone.getText().toString();

                if(phone.length() != 10 || phone.matches("^[0-9]+$") == false)
                    Toast.makeText(Them.this, "Hãy nhập số điện thoại hợp lệ", Toast.LENGTH_LONG).show();
                else
                {
                    Intent intent = new Intent();
                    Bundle b = new Bundle();
                    b.putInt("Id", id);
                    b.putString("Name", name);
                    b.putString("Phone", phone);

                    intent.putExtras(b);
                    setResult(150, intent);
                    finish();
                }

                }

        });


    }
//    public boolean validatePhone(EditText phone) {
//        String regexStr = "^[0-9]$";
//        String number=phone.getText().toString();
//        if(number.toString().length() < 10 || number.toString().length() > 13 || number.matches(regexStr) == false) {
//            return false;
//        }
//        return true;
//    }


}