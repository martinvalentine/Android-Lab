package com.example.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDB extends SQLiteOpenHelper{
   public static final String TableName = "ContactTable";
    public static final String Id = "Id";
    public static final String Name = "Name";
    public static final String PhoneNumber = "PhoneNumber";
    public static final String Email = "Email";
    public static final String Picture = "Picture";

    // Constructor to create a database
    public MyDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table by SQL
        String sql = "CREATE TABLE IF NOT EXISTS " + TableName + " (" +
                Id + " INTEGER PRIMARY KEY, " +
                Name + " TEXT, " +
                PhoneNumber + " TEXT, " +
                Email + " TEXT, " +
                Picture + " TEXT)";

        // Execute SQL
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        // Create table again
        onCreate(db);
    }

    // Create addContact method
    public void addContact(Person person)
    {
        // Get database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create ContentValues to store data
        ContentValues values = new ContentValues();
        // Put data to ContentValues
        values.put(Id, person.getId());
        values.put(Name, person.getName());
        values.put(PhoneNumber, person.getPhoneNumber());
        values.put(Email, person.getEmail());
        values.put(Picture, person.getPicture());
        // Insert data to database
        db.insert(TableName, null, values);
        db.close();
    }

    // Get all data in table TableContact and return as Arraylist
    public ArrayList<Person> getAllContact()
    {
        ArrayList<Person> list = new ArrayList<>();
        // SQL command
        String sql = "SELECT * FROM " + TableName;
        // Get Object in sqLite database
        SQLiteDatabase db = this.getReadableDatabase();
        // Run SQL command
        Cursor cursor = db.rawQuery(sql, null);
        // Create ArrayList<Person> to return
        if(cursor!=null)
        {
            while(cursor.moveToNext())
            {
                Person person = new Person((cursor.getInt(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), false);
                list.add(person);
            }
        }
        cursor.close();
        db.close();
        return list;
    }

    // Delete a contact by ID
    public void deleteContact(int id)
    {
        // Get database
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete a contact by ID
        String sql = "DELETE FROM " + TableName + " WHERE " + Id + " = " + id;
        db.execSQL(sql);
        db.close();
    }

    public void updateContact(Person item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Name, item.getName());
        values.put(PhoneNumber, item.getPhoneNumber());
        values.put(Email, item.getEmail());
        values.put(Picture, item.getPicture());
        db.update(TableName, values, Id + " = ?", new String[]{String.valueOf(item.getId())});
        db.close();
    }

    public void deleteAllContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TableName);
        db.close();
    }
}
