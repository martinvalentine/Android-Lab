package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    EditText id;
    EditText name;
    EditText phoneNumber;
    EditText email;
    ImageView picture;
    Button saveButton;
    Button cancelButton;
    String imageUri;
    MyDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_CONTACTS},
                PackageManager.PERMISSION_GRANTED);

        // initialize the database
        db = new MyDB(this, "ContactDB", null, 1);

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

                // Add contact to Contact Application
                buttonAddContact(v);

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

    // Add contact to phone
    public void buttonAddContact(View view){
        ArrayList<ContentProviderOperation> contentProviderOperations
                = new ArrayList<ContentProviderOperation>();

        // Adding insert operation to operations list for raw contact type
        contentProviderOperations.add(ContentProviderOperation.newInsert(
                        ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());
    // ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI):
        // This creates a new ContentProviderOperation instance to insert a new raw contact.
        // The ContactsContract.RawContacts.CONTENT_URI is the URI for the raw contacts table in the Contacts content provider.
    //.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null): This specifies the account type for the raw contact.
        // In this case, it is set to null, which means it is a device local contact (not associated with any account).
    //.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null):
        // This specifies the account `name for the raw contact. Again, it is set to null, indicating that it is a device local contact.
    //.build():
        // This builds the ContentProviderOperation instance.
    //contentProviderOperations.add(...):
        // This adds the built ContentProviderOperation instance to the contentProviderOperations ArrayList.

        // Adding Name
        contentProviderOperations.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        name.getText().toString())
                .build());

        // Adding Number
        contentProviderOperations.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                        phoneNumber.getText().toString())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .build());

        // Adding Email
        contentProviderOperations.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA,
                        email.getText().toString())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());

        // Adding Image
        try {
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(imageUri));
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] data = stream.toByteArray();
                contentProviderOperations.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, data)
                        .build());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Add contact to phone
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
            Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show();
        }
    }

}