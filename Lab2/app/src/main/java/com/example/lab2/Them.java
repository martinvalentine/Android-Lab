package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private String img_path;
    private static final int PICK_IMAGE_REQUEST = 1;

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to pick an image from the device's gallery
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
                pickImageIntent.setType("image/*");

                // Start the activity to select an image
                startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
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
                    b.putString("Image", img_path);
                    intent.putExtras(b);
                    setResult(150, intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the URI of the selected image
            Uri selectedImageUri = data.getData();
            img_path = getRealPathFromURI(selectedImageUri);
            // Set the selected image to the ImageView
            imageView.setImageURI(selectedImageUri);
        }
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }
}
