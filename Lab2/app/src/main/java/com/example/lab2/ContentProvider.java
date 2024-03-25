package com.example.lab2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class ContentProvider {
    private Activity activity;
    public ContentProvider(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Person> getAllContact(){
        ArrayList<Person> listPerson = new ArrayList<>();
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI
        };
        Cursor cursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        if(cursor.moveToFirst())
        {
            do {
                Person person = new Person(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), false);
                listPerson.add(person);
            } while (cursor.moveToNext());
        }
        return listPerson;
    }
}
