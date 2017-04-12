package com.dev.lakik.expresspos.Database.ModelHelpers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Model.Transaction;
import com.dev.lakik.expresspos.Model.TransactionProduct;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Pasha on 07.11.2016.
 */

public class TransactionProductHelper {

    public static String TABLE_NAME = "TransactionProducts";

    //Define all columns and their type
    public static String NAME_COLUMN_TRANSACTION_ID = "transaction_id";
    public static String TYPE_COLUMN_TRANSACTION_ID = "text";

    public static String NAME_COLUMN_PRODUCT_ID = "product_id";
    public static String TYPE_COLUMN_PRODUCT_ID = "text";

    public static String NAME_COLUMN_PRICE = "price";
    public static String TYPE_COLUMN_PRICE = "DECIMAL(16,2)";

    public static String NAME_COLUMN_AMOUNT = "amount";
    public static String TYPE_COLUMN_AMOUNT = "integer";


    SQLiteDatabase mDB;

    //Function that create create string
    public static String getCreateQuery(){

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TABLE_NAME);
        sb.append(" (");
        sb.append(NAME_COLUMN_TRANSACTION_ID + " " + TYPE_COLUMN_TRANSACTION_ID + ", ");
        sb.append(NAME_COLUMN_PRODUCT_ID + " " + TYPE_COLUMN_PRODUCT_ID + ", ");
        sb.append(NAME_COLUMN_PRICE + " " + TYPE_COLUMN_PRICE + ", ");
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
    public static ArrayList<TransactionProduct> getAllRecordsByID(String id){
        SQLiteDatabase db = DBHelper.Instance().getDB();

        ArrayList<TransactionProduct> tempArray = new ArrayList<>();
        Cursor c = db.rawQuery("Select * from " + TABLE_NAME + " where " + NAME_COLUMN_TRANSACTION_ID + "= '" + id + "'", null);
        while(c.moveToNext()){
            TransactionProduct item = new TransactionProduct();
            item.setTransactionId(UUID.fromString(c.getString(c.getColumnIndex(NAME_COLUMN_TRANSACTION_ID))));
            item.setProductId(UUID.fromString(c.getString(c.getColumnIndex(NAME_COLUMN_PRODUCT_ID))));
            item.setPrice(c.getFloat(c.getColumnIndex(NAME_COLUMN_PRICE)));
            item.setAmount(c.getInt(c.getColumnIndex(NAME_COLUMN_AMOUNT)));

            item.loadProduct();

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
