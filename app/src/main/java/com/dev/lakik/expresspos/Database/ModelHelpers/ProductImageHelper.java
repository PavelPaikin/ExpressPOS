package com.dev.lakik.expresspos.Database.ModelHelpers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Model.Product;
import com.dev.lakik.expresspos.Model.ProductImage;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Pasha on 07.11.2016.
 */

public class ProductImageHelper {

    public static String TABLE_NAME = "ProductImage";

    //Define all columns and their type
    public static String NAME_COLUMN_PRODUCT_ID = "product_id";
    public static String TYPE_COLUMN_PRODUCT_ID = "text";

    public static String NAME_COLUMN_IMAGE_PATH = "imagePath";
    public static String TYPE_COLUMN_IMAGE_PATH = "text";

    SQLiteDatabase mDB;

    //Function that create create string
    public static String getCreateQuery(){

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TABLE_NAME);
        sb.append(" (");
        sb.append(NAME_COLUMN_PRODUCT_ID + " " + TYPE_COLUMN_PRODUCT_ID + ", ");
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
    public static ArrayList<ProductImage> getAllRecords(){
        SQLiteDatabase db = DBHelper.Instance().getDB();

        ArrayList<ProductImage> tempArray = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, NAME_COLUMN_PRODUCT_ID + " DESC");
        while(c.moveToNext()){
            ProductImage item = new ProductImage();
            item.setProduct_id(UUID.fromString(c.getString(c.getColumnIndex(NAME_COLUMN_PRODUCT_ID))));
            item.setImagePath(c.getString(c.getColumnIndex(NAME_COLUMN_IMAGE_PATH)));

            tempArray.add(item);
        }
        c.close();

        return tempArray;
    }

    public static ArrayList<ProductImage> getAllRecordsForProduct(String id){
        SQLiteDatabase db = DBHelper.Instance().getDB();

        ArrayList<ProductImage> tempArray = new ArrayList<>();
        Cursor c = db.rawQuery("Select * from " + TABLE_NAME + " where " + NAME_COLUMN_PRODUCT_ID + "= '" + id + "'", null);
        while(c.moveToNext()){
            ProductImage item = new ProductImage();
            item.setProduct_id(UUID.fromString(c.getString(c.getColumnIndex(NAME_COLUMN_PRODUCT_ID))));
            item.setImagePath(c.getString(c.getColumnIndex(NAME_COLUMN_IMAGE_PATH)));

            tempArray.add(item);
        }
        c.close();

        return tempArray;
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
