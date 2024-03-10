package com.example.lab2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    EditText id;
    EditText name;
    EditText phoneNumber;
    EditText email;
    ImageView picture;
    Button saveChangeButton;
    Button cancelChangeButton;
    String imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // initialize the views
        id = findViewById(R.id.contactID);
        name = findViewById(R.id.contactChangeName);
        phoneNumber = findViewById(R.id.contactChangePhoneNumber);
        email = findViewById(R.id.contactChangeEmail);
        picture = findViewById(R.id.contactChangeImage);
        saveChangeButton = findViewById(R.id.btn_saveChange);
        cancelChangeButton = findViewById(R.id.btn_cancelChange);

        // Make ID uneditable
        id.setEnabled(false);

        // set the on click listener to the cancel button to back to the main activity
        cancelChangeButton.setOnClickListener(v -> {
            finish();
        });

        // Show data from the Main activity to the Edit activity
        Bundle data = getIntent().getExtras();
        if (data != null) {
            id.setText(String.valueOf(data.getInt("ID")));
            name.setText(data.getString("Name"));
            phoneNumber.setText(data.getString("PhoneNumber"));
            email.setText(data.getString("Email"));
            imageUri = data.getString("Image");
            picture.setImageURI(Uri.parse(imageUri));
        }

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

        // set the on click listener to the save button
        saveChangeButton.setOnClickListener(new View.OnClickListener() {
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
                setResult(250, intent); // Result code
                finish();
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
