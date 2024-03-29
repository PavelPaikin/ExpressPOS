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

    public static String NAME_COLUMN_COMPANY_ID = "company_id";
    public static String TYPE_COLUMN_COMPANY_ID = "text";

    public static String NAME_COLUMN_NAME = "name";
    public static String TYPE_COLUMN_NAME = "text";

    public static String NAME_COLUMN_NUMBER = "number";
    public static String TYPE_COLUMN_NUMBER = "text";

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
        sb.append(NAME_COLUMN_COMPANY_ID + " " + TYPE_COLUMN_COMPANY_ID + ", ");
        sb.append(NAME_COLUMN_NAME + " " + TYPE_COLUMN_NAME + ", ");
        sb.append(NAME_COLUMN_NUMBER + " " + TYPE_COLUMN_NUMBER + ", ");
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
    public static ArrayList<Product> getAllRecords(String companyID){
        SQLiteDatabase db = DBHelper.Instance().getDB();

        ArrayList<Product> tempArray = new ArrayList<>();
        //Cursor c = db.rawQuery("Select * from " + TABLE_NAME + " where " + NAME_COLUMN_COMPANY_ID + "= '" + companyID + "'", null);
        Cursor c = db.rawQuery("Select * from " + TABLE_NAME, null);
        logCursor(c);
        while(c.moveToNext()){
            Product item = new Product(companyID);
            item.setId(c.getString(c.getColumnIndex(NAME_COLUMN_ID)));
            item.setCompanyId(c.getString(c.getColumnIndex(NAME_COLUMN_COMPANY_ID)));
            item.setName(c.getString(c.getColumnIndex(NAME_COLUMN_NAME)));
            item.setNumber(c.getString(c.getColumnIndex(NAME_COLUMN_NUMBER)));
            item.setUpc(c.getString(c.getColumnIndex(NAME_COLUMN_UPC)));
            item.setDescription(c.getString(c.getColumnIndex(NAME_COLUMN_DESCRIPTION)));
            item.setPrice(c.getDouble(c.getColumnIndex(NAME_COLUMN_PRICE)));

            tempArray.add(item);
        }
        c.close();

        return tempArray;
    }

    //Get item by id
    public static Product get(String id, String companyID){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        getAllRecords(companyID);

        Log.d("sadas", "companyID: " + companyID + " Product ID: " + id);
        Cursor c = db.rawQuery("Select * from " + TABLE_NAME + " where " + NAME_COLUMN_ID + "= '" + id + "' AND " + NAME_COLUMN_COMPANY_ID + "= '" + companyID + "'", null);
        Product item = null;
        if(c.getCount() != 0) {
            c.moveToFirst();

            item = new Product(companyID);
            item.setId(c.getString(c.getColumnIndex(NAME_COLUMN_ID)));
            item.setCompanyId(c.getString(c.getColumnIndex(NAME_COLUMN_COMPANY_ID)));
            item.setName(c.getString(c.getColumnIndex(NAME_COLUMN_NAME)));
            item.setNumber(c.getString(c.getColumnIndex(NAME_COLUMN_NUMBER)));
            item.setUpc(c.getString(c.getColumnIndex(NAME_COLUMN_UPC)));
            item.setDescription(c.getString(c.getColumnIndex(NAME_COLUMN_DESCRIPTION)));
            item.setPrice(c.getDouble(c.getColumnIndex(NAME_COLUMN_PRICE)));

            item.loadImages();

            c.close();
        }
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
