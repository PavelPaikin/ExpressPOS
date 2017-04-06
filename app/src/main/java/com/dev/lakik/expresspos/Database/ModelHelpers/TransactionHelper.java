package com.dev.lakik.expresspos.Database.ModelHelpers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Model.Inventory;
import com.dev.lakik.expresspos.Model.Transaction;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Pasha on 07.11.2016.
 */

public class TransactionHelper {

    public static String TABLE_NAME = "Transaction";

    //Define all columns and their type
    public static String NAME_COLUMN_ID = "id";
    public static String TYPE_COLUMN_ID = "text";

    public static String NAME_COLUMN_DATE = "date";
    public static String TYPE_COLUMN_DATE = "date";

    public static String NAME_COLUMN_SUB_TOTAL = "sub_total";
    public static String TYPE_COLUMN_SUB_TOTAL = "DECIMAL(16,2)";

    public static String NAME_COLUMN_TAX = "tax";
    public static String TYPE_COLUMN_TAX = "DECIMAL(16,2)";

    public static String NAME_COLUMN_TOTAL = "total";
    public static String TYPE_COLUMN_TOTAL = "DECIMAL(16,2)";

    SQLiteDatabase mDB;

    //Function that create create string
    public static String getCreateQuery(){

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TABLE_NAME);
        sb.append(" (");
        sb.append(NAME_COLUMN_ID + " " + TYPE_COLUMN_ID + " primary key, ");
        sb.append(NAME_COLUMN_DATE + " " + TYPE_COLUMN_DATE + ", ");
        sb.append(NAME_COLUMN_SUB_TOTAL + " " + TYPE_COLUMN_SUB_TOTAL + ", ");
        sb.append(NAME_COLUMN_TAX + " " + TYPE_COLUMN_TAX + ", ");
        sb.append(NAME_COLUMN_TOTAL + " " + TYPE_COLUMN_TOTAL + ")");

        return sb.toString();
    }

    //Drops table
    public static void dropTable(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(getCreateQuery());
    }

    //Get all items and return Array list with all items
    public static ArrayList<Transaction> getAllRecords(){
        SQLiteDatabase db = DBHelper.Instance().getDB();

        ArrayList<Transaction> tempArray = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, NAME_COLUMN_DATE + " DESC");
        while(c.moveToNext()){
            Transaction item = new Transaction();
            item.setId(UUID.fromString(c.getString(0)));
            item.setDate(c.getString(1));
            item.setSubTotal(c.getFloat(2));
            item.setTax(c.getFloat(3));
            item.setTotal(c.getFloat(4));

            item.loadProducts();

            tempArray.add(item);
        }
        c.close();

        return tempArray;
    }

    //Get item by id
    public static Transaction get(String id){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        Cursor c = db.rawQuery("Select * from " + TABLE_NAME + " where " + NAME_COLUMN_ID + "= '" + id + "'", null);
        if(c!=null) c.moveToFirst();

        Transaction item = new Transaction();
        item.setId(UUID.fromString(c.getString(0)));
        item.setDate(c.getString(1));
        item.setSubTotal(c.getFloat(2));
        item.setTax(c.getFloat(3));
        item.setTotal(c.getFloat(4));

        item.loadProducts();

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
