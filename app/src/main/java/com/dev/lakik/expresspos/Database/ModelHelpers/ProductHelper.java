package com.dev.lakik.expresspos.Database.ModelHelpers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Model.Company;
import com.dev.lakik.expresspos.Model.Product;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Pasha on 07.11.2016.
 */

public class ProductHelper {

    public static String TABLE_NAME = "Product";

    //Define all columns and their type
    public static String NAME_COLUMN_ID = "id";
    public static String TYPE_COLUMN_ID = "text";

    public static String NAME_COLUMN_NAME = "name";
    public static String TYPE_COLUMN_NAME = "text";

    public static String NAME_COLUMN_UPC = "upc";
    public static String TYPE_COLUMN_UPC = "text";

    public static String NAME_COLUMN_DESCRIPTION = "description";
    public static String TYPE_COLUMN_DESCRIPTION = "text";

    public static String NAME_COLUMN_PRICE = "price";
    public static String TYPE_COLUMN_PRICE = "DECIMAL(16,2)";

    SQLiteDatabase mDB;

    //Function that create create string
    public static String getCreateQuery(){

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TABLE_NAME);
        sb.append(" (");
        sb.append(NAME_COLUMN_ID + " " + TYPE_COLUMN_ID + " primary key, ");
        sb.append(NAME_COLUMN_NAME + " " + TYPE_COLUMN_NAME + ", ");
        sb.append(NAME_COLUMN_UPC + " " + TYPE_COLUMN_UPC + ", ");
        sb.append(NAME_COLUMN_DESCRIPTION + " " + TYPE_COLUMN_DESCRIPTION + ", ");
        sb.append(NAME_COLUMN_PRICE + " " + TYPE_COLUMN_PRICE + ")");

        return sb.toString();
    }

    //Drops table
    public static void dropTable(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(getCreateQuery());
    }

    //Get all items and return Array list with all items
    public static ArrayList<Product> getAllRecords(){
        SQLiteDatabase db = DBHelper.Instance().getDB();

        ArrayList<Product> tempArray = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, NAME_COLUMN_ID + " DESC");
        while(c.moveToNext()){
            Product item = new Product();
            item.setId(UUID.fromString(c.getString(0)));
            item.setName(c.getString(1));
            item.setUpc(c.getString(2));
            item.setDescription(c.getString(3));
            item.setPrice(c.getDouble(4));

            tempArray.add(item);
        }
        c.close();

        return tempArray;
    }

    //Get item by id
    public static Product get(String id){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        Cursor c = db.rawQuery("Select * from " + TABLE_NAME + " where " + NAME_COLUMN_ID + "= '" + id + "'", null);
        if(c!=null) c.moveToFirst();

        Product item = new Product();
        item.setId(UUID.fromString(c.getString(0)));
        item.setName(c.getString(1));
        item.setUpc(c.getString(2));
        item.setDescription(c.getString(3));
        item.setPrice(c.getDouble(4));

        item.loadImages();

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
