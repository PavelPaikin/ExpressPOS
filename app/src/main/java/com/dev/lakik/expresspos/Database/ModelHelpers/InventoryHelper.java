package com.dev.lakik.expresspos.Database.ModelHelpers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Model.Inventory;
import com.dev.lakik.expresspos.Model.Product;
import com.dev.lakik.expresspos.Model.ProductImage;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Pasha on 07.11.2016.
 */

public class InventoryHelper {

    public static String TABLE_NAME = "Inventory";

    //Define all columns and their type
    public static String NAME_COLUMN_ID = "id";
    public static String TYPE_COLUMN_ID = "text";

    public static String NAME_COLUMN_PRODUCT_ID = "product_id";
    public static String TYPE_COLUMN_PRODUCT_ID = "text";

    public static String NAME_COLUMN_AMOUNT = "amount";
    public static String TYPE_COLUMN_AMOUNT = "integer";

    SQLiteDatabase mDB;

    //Function that create create string
    public static String getCreateQuery(){

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TABLE_NAME);
        sb.append(" (");
        sb.append(NAME_COLUMN_ID + " " + TYPE_COLUMN_ID + " primary key, ");
        sb.append(NAME_COLUMN_PRODUCT_ID + " " + TYPE_COLUMN_PRODUCT_ID + ", ");
        sb.append(NAME_COLUMN_AMOUNT + " " + TYPE_COLUMN_AMOUNT + ")");

        return sb.toString();
    }

    //Drops table
    public static void dropTable(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(getCreateQuery());
    }

    //Get all items and return Array list with all items
    public static ArrayList<Inventory> getAllRecords(){
        SQLiteDatabase db = DBHelper.Instance().getDB();

        ArrayList<Inventory> tempArray = new ArrayList<>();
        Cursor c = db.rawQuery("Select * from " + TABLE_NAME + " where " + NAME_COLUMN_AMOUNT + "> 0", null);
        while(c.moveToNext()){
            Inventory item = new Inventory();
            item.setId(UUID.fromString(c.getString(0)));
            item.setProduct_id(UUID.fromString(c.getString(1)));
            item.setAmount(c.getInt(2));

            item.loadProduct();

            tempArray.add(item);
        }
        c.close();

        return tempArray;
    }

    //Get item by id
    public static Inventory get(String id){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        Cursor c = db.rawQuery("Select * from " + TABLE_NAME + " where " + NAME_COLUMN_ID + "= '" + id + "'", null);
        if(c!=null) c.moveToFirst();

        Inventory item = new Inventory();
        item.setId(UUID.fromString(c.getString(0)));
        item.setProduct_id(UUID.fromString(c.getString(1)));
        item.setAmount(c.getInt(2));

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
