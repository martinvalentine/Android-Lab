package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;

import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ContactAdapter contactAdapter;
    ArrayList<Person> listContact;
    EditText search;
    ListView personList;
    Button addButton;
    Button deleteButton;
    int selectedItem;
    String imageUri;
    MyDB db;

    // Request code for READ_CONTACTS. It can be any number
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1234;

    // declare Content Provider
    ContentProvider contentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the views
        search = findViewById(R.id.edittext_search);
        personList = findViewById(R.id.listContact);
        addButton = findViewById(R.id.btn_add);
        deleteButton = findViewById(R.id.btn_delete);

        // Register context menu for List contact
        registerForContextMenu(personList);

        // Create a new database
        db = new MyDB(this, "ContactDB", null, 1);

        // Remove all items in the database
//        db.deleteAllContact();
        // create a new array list
        listContact = new ArrayList<>();

        // add data to the array list
//        db.addContact(new Person(1, "John Doe", "1234567890", "ntq@gmail.com", "android.resource://com.example.lab2/drawable/cato1", false));
//        db.addContact((new Person(2, "Jane Doe", "1234567890", "jqkat@gmail.com", "android.resource://com.example.lab2/drawable/cato2", false)));
//        db.addContact((new Person(3, "John Smith", "1234567890", "akoyeuem@gmail.com", "android.resource://com.example.lab2/drawable/cato3", false)));
//        db.addContact((new Person(4, "Jane Smith", "1234567890", "meoOggy@gmail.com", "android.resource://com.example.lab2/drawable/cato4", false)));
//        db.addContact((new Person(5, "Justin Bieber", "1234567890", "meoOggy@gmail.com", "android.resource://com.example.lab2/drawable/cato5", false)));
//        db.addContact((new Person(6, "The Chainsmokers", "1234567890", "meoOggy@gmail.com", "android.resource://com.example.lab2/drawable/cato6", false)));
//        db.addContact((new Person(7, "G-Eazy", "1234567890", "meoOggy@gmail.com", "android.resource://com.example.lab2/drawable/cato7", false)));
//        db.addContact((new Person(8, "Kendrick Lamar", "1234567890", "meoOggy@gmail.com", "android.resource://com.example.lab2/drawable/cato8", false)));

        // Return data to listContact
//        listContact = db.getAllContact();

        // Show contact from content provider Contact application
        showContact();

        // create a new contact adapter
        contactAdapter = new ContactAdapter(listContact, this);

        // set the adapter to the list view
        personList.setAdapter(contactAdapter);
        personList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = position;
                return false;
            }
        });

        // set the on click listener to the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class); // Bridge between 2 Activity
                startActivityForResult(intent,150);
            }
        });

        // set the on click listener to the delete button to delete the checked item
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Person> phanTuCanLoaiBo = new ArrayList<>();

                for (int i = listContact.size() - 1; i >= 0; i--) {
                    if (listContact.get(i).isCheck()) {
                        phanTuCanLoaiBo.add(listContact.get(i));
                    }
                }

                AlertDialog.Builder dg= new AlertDialog.Builder(MainActivity.this);
                dg.setTitle("Thông báo");
                dg.setMessage("Bạn có chắc chắn muốn xóa không?");
                dg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listContact.removeAll(phanTuCanLoaiBo);
                        for (Person p : phanTuCanLoaiBo) {
                            db.deleteContact(p.getId());
                        }
                        personList.setAdapter(contactAdapter);

                    }
                });
                dg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog al = dg.create();
                al.show();
            }
        });

        // TO-DO: Handle the search button
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactAdapter.getFilter().filter(s);
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // Handle the result from the AddActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            Bundle b = data.getExtras();
            int id = b.getInt("ID");
            String fullName = b.getString("Name");
            String phone = b.getString("PhoneNumber");
            String imageuri = b.getString("Image");
            String email = b.getString("Email");
            Person item = new Person(id, fullName, phone, email, imageuri, false);
            if(requestCode == 150 && resultCode == 200){
                listContact.add(item);
                db.addContact(item);
                contactAdapter.notifyDataSetChanged();
            } else if(resultCode == RESULT_CANCELED){
                Log.v("Error", "SomeThing error");
            } else if (requestCode == 175 && resultCode == 250) {
                // Set the data has changed from the EditActivity to the item in listContact
                listContact.get(selectedItem).setName(fullName);
                listContact.get(selectedItem).setPhoneNumber(phone);
                listContact.get(selectedItem).setEmail(email);
                listContact.get(selectedItem).setPicture(imageuri);
                db.updateContact(item);
                contactAdapter.notifyDataSetChanged();

            }
        } else if(resultCode == RESULT_CANCELED){
            // Do nothing
        }
    }

    // Create menu context
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_context,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // Get the current selected Person
        Person p = listContact.get(selectedItem);

        // Handle selected item
        if (item.getItemId() == R.id.menuDelete) {
            // Show dialog to confirm delete
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete");
            builder.setMessage("Do you want to delete this contact?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Remove the selected item from the list
                listContact.remove(selectedItem);
//                db.deleteContact(p.getId());
                deleteContact(p.getId());
                contactAdapter.notifyDataSetChanged();
            });

            builder.setNegativeButton("No", (dialog, which) -> {
                // Do nothing if the user selects "No"
            });

            // Show the dialog
            builder.show();
        } else if (item.getItemId() == R.id.menuEdit) // handle the edit item
        {
            imageUri = p.getPicture();
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            Bundle dataPack = new Bundle();
            // Pass data to the EditActivity
            dataPack.putInt("ID", p.getId());
            dataPack.putString("Name", p.getName());
            dataPack.putString("Email", p.getEmail());
            dataPack.putString("PhoneNumber", p.getPhoneNumber());
            dataPack.putString("Image", imageUri);
            intent.putExtras(dataPack);
            startActivityForResult(intent, 175);
        } else if (item.getItemId() == R.id.menuCall) {
            // Hanlded the call item to redirect to the phone app to call the phone of the item
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + p.getPhoneNumber()));
            startActivity(intent);

        } else if (item.getItemId() == R.id.menuMail) {
            // Handle the mail item to redirect to the mail app to send a mail to the email of the item
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + p.getEmail()));
            startActivity(intent);

        } else if (item.getItemId() == R.id.menuSMS){
            // Handle the SMS item to redirect to the message app to send a message to the phone number of the item
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + p.getPhoneNumber()));
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuFacebook) {
            // Handle the Facebook item to redirect to the Facebook app to send a message to the phone number of the item
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(Uri.parse("https://www.facebook.com/") + p.getName().toLowerCase().toString()));
            startActivity(intent);
        }
        return super.onContextItemSelected(item);
    }

    // Handle content provider
    private void showContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            // After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            contentProvider = new ContentProvider(this);
            listContact = contentProvider.getAllContact();
            contactAdapter = new ContactAdapter(listContact, this);
            personList.setAdapter(contactAdapter);
        }
    }

    // TODO: Handle delete contact directly to Contact Application
    // Delete contact from phone
    public void deleteContact(long rawContactId) {
        ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<>();

        // Delete the raw contact
        contentProviderOperations.add(ContentProviderOperation.newDelete(
            ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, rawContactId))
                .build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}