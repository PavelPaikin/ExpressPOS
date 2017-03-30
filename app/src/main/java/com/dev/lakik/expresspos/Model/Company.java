package com.dev.lakik.expresspos.Model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.CompanyHelper;

import java.util.UUID;

/**
 * Created by ppash on 08.02.2017.
 */

public class Company extends CompanyHelper {

    private UUID id;
    private String name;
    private String login;
    private String password;
    private String email;
    private String imagePath;

    public Company(){
        this.id = UUID.randomUUID();
    }

    public Company(String name, String login, String password, String email, String imagePath) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.imagePath = imagePath;
    }

    public Company(String id, String name, String login, String password, String email, String imagePath) {
        this.id = UUID.fromString(id);
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.imagePath = imagePath;
    }

    //Save current item to database if exists updates record
    public void save(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        ContentValues cv = new ContentValues();
        
        cv.put(NAME_COLUMN_ID, this.id.toString());
        cv.put(NAME_COLUMN_NAME, this.name);
        cv.put(NAME_COLUMN_LOGIN, this.login);
        cv.put(NAME_COLUMN_PASSWORD, this.password);
        cv.put(NAME_COLUMN_EMAIL, this.email);
        cv.put(NAME_COLUMN_IMAGE_PATH, this.imagePath);

        long id = db.replace(TABLE_NAME, null, cv);
        Log.d("DBHelper", "inserted id: " + id);
    }

    //delete curent item from table
    public void delete(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + NAME_COLUMN_ID + " = '" + this.id + "'");
    }

    public void printObject(){
        StringBuilder sb = new StringBuilder();
        sb.append(id.toString() + ", ");
        sb.append(name + ", ");
        sb.append(login + ", ");
        sb.append(password + ", ");
        sb.append(email + ", ");
        sb.append(imagePath);

        Log.d("ObjectFields", "Company: " + sb.toString());
    }

    public String getId() { return id.toString(); }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
