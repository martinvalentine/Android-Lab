package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddActivity extends AppCompatActivity {
    EditText id;
    EditText name;
    EditText phoneNumber;
    EditText email;
    ImageView picture;
    Button saveButton;
    Button cancelButton;
    String imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // initialize the views
        id = findViewById(R.id.contactID);
        name = findViewById(R.id.contactAddName);
        phoneNumber = findViewById(R.id.contactAddPhoneNumber);
        email = findViewById(R.id.contactAddEmail);
        saveButton = findViewById(R.id.btn_save);
        cancelButton = findViewById(R.id.btn_cancel);
        picture = findViewById(R.id.contactAddImage);


        // set the on click listener to the cancel button
        cancelButton.setOnClickListener(v -> {
            finish();
        });

        // set the on click listener to the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle dataPack = new Bundle();
                dataPack.putInt("ID", Integer.parseInt(id.getText().toString()));
                dataPack.putString("Name", name.getText().toString());
                dataPack.putString("Email", email.getText().toString());
                dataPack.putString("PhoneNumber", phoneNumber.getText().toString());
                dataPack.putString("Image", imageUri);
                intent.putExtras(dataPack);
                setResult(200, intent); // Result code
                finish();
            }
        });

        // set the on click listener to the picture
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*"); // set the type of the intent to image
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
            }
        });


    }

    // handle the result from the picture intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData().toString();
            picture.setImageURI(data.getData());
        }
    }
}