package com.dev.lakik.expresspos.Database.ModelHelpers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Model.Company;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Pasha on 07.11.2016.
 */

public class CompanyHelper {

    public static String TABLE_NAME = "Company";

    //Define all columns and their type
    public static String NAME_COLUMN_ID = "id";
    public static String TYPE_COLUMN_ID = "text";

    public static String NAME_COLUMN_NAME = "name";
    public static String TYPE_COLUMN_NAME = "text";

    public static String NAME_COLUMN_LOGIN = "login";
    public static String TYPE_COLUMN_LOGIN = "text";

    public static String NAME_COLUMN_PASSWORD = "password";
    public static String TYPE_COLUMN_PASSWORD = "text";

    public static String NAME_COLUMN_EMAIL = "email";
    public static String TYPE_COLUMN_EMAIL = "text";

    public static String NAME_COLUMN_IMAGE_PATH = "imagePath";
    public static String TYPE_COLUMN_IMAGE_PATH = "text";

    SQLiteDatabase mDB;

    //Function that create create string
    public static String getCreateQuery(){

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TABLE_NAME);
        sb.append(" (");
        sb.append(NAME_COLUMN_ID + " " + TYPE_COLUMN_ID + " primary key, ");
        sb.append(NAME_COLUMN_NAME + " " + TYPE_COLUMN_NAME + ", ");
        sb.append(NAME_COLUMN_LOGIN + " " + TYPE_COLUMN_LOGIN + ", ");
        sb.append(NAME_COLUMN_PASSWORD + " " + TYPE_COLUMN_PASSWORD + ", ");
        sb.append(NAME_COLUMN_EMAIL + " " + TYPE_COLUMN_EMAIL + ", ");
        sb.append(NAME_COLUMN_IMAGE_PATH + " " + TYPE_COLUMN_IMAGE_PATH + ")");

        return sb.toString();
    }

    //Drops table
    public static void dropTable(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(getCreateQuery());
    }

    //Get all items and return Array list with all items
    public static ArrayList<Company> getAllRecords(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        ArrayList<Company> tempArray = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, NAME_COLUMN_ID + " DESC");
        while(c.moveToNext()){
            Company item = new Company();
            item.setId(UUID.fromString(c.getString(c.getColumnIndex(NAME_COLUMN_ID))));
            item.setName(c.getString(c.getColumnIndex(NAME_COLUMN_NAME)));
            item.setLogin(c.getString(c.getColumnIndex(NAME_COLUMN_LOGIN)));
            item.setPassword(c.getString(c.getColumnIndex(NAME_COLUMN_PASSWORD)));
            item.setEmail(c.getString(c.getColumnIndex(NAME_COLUMN_EMAIL)));
            item.setImagePath(c.getString(c.getColumnIndex(NAME_COLUMN_IMAGE_PATH)));

            tempArray.add(item);
        }
        c.close();

        return tempArray;
    }

    //Get item by id
    public static Company get(String id){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        Cursor c = db.rawQuery("Select * from " + TABLE_NAME + " where " + NAME_COLUMN_ID + "= '" + id + "'", null);
        if(c!=null) c.moveToFirst();

        Company item = new Company();
        item.setId(UUID.fromString(c.getString(c.getColumnIndex(NAME_COLUMN_ID))));
        item.setName(c.getString(c.getColumnIndex(NAME_COLUMN_NAME)));
        item.setLogin(c.getString(c.getColumnIndex(NAME_COLUMN_LOGIN)));
        item.setPassword(c.getString(c.getColumnIndex(NAME_COLUMN_PASSWORD)));
        item.setEmail(c.getString(c.getColumnIndex(NAME_COLUMN_EMAIL)));
        item.setImagePath(c.getString(c.getColumnIndex(NAME_COLUMN_IMAGE_PATH)));

        c.close();
        return item;
    }



    public static void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d("aaa", str);
                } while (c.moveToNext());
            }
        } else{
            Log.d("aaa", "Cursor is null");
        }
    }

}
