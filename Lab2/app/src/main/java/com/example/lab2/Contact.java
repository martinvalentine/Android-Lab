package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;

public class Contact{

    private int Id;
    private String Name;
    private String Phone;
    private boolean Status;
    private String Image;
    public Contact(){}
    public Contact(int id, String name, String phone, boolean status, String image) {
        Id = id;
        Name = name;
        Phone = phone;
        Status = status;
        Image = image;
    }

    public int getId(){return Id;}
    public void setId(int Id){ this.Id = Id;}

    public String getName(){return Name;}
    public void setName(String Name){this.Name = Name;}
    public String getPhone(){return Phone;}
    public void setPhone(String Phone){this.Phone = Phone;}
    public boolean getStatus(){return Status;}
    public void setStatus(boolean Status){this.Status = Status;}
    public String getImage(){return Image;}
    public void setImage(String Image){this.Image = Image;}
}