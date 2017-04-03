package com.dev.lakik.expresspos.Model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.InventoryHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.TransactionHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ppash on 08.02.2017.
 */

public class Transaction extends TransactionHelper {

    private UUID id;
    private String date;
    private float subTotal;
    private float tax;
    private float total;

    private ArrayList<TransactionProduct> arrTransProducts;

    public Transaction(){}

    public Transaction(String date, float subTotal, float tax, float total) {
        this.id = UUID.randomUUID();
        this.date = date;
        this.subTotal = subTotal;
        this.tax = tax;
        this.total = total;

    }

    public Transaction(UUID id, String date, float subTotal, float tax, float total) {
        this.id = UUID.randomUUID();
        this.date = date;
        this.subTotal = subTotal;
        this.tax = tax;
        this.total = total;
    }

    public void loadProducts(){
        this.arrTransProducts = TransactionProduct.getAllRecordsByID(id.toString());
    }

    //Save current item to database if exists updates record
    public void save(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        ContentValues cv = new ContentValues();

        cv.put(NAME_COLUMN_ID, this.id.toString());
        cv.put(NAME_COLUMN_DATE, this.date);
        cv.put(NAME_COLUMN_SUB_TOTAL, this.subTotal);
        cv.put(NAME_COLUMN_TAX, this.tax);
        cv.put(NAME_COLUMN_TOTAL, this.total);

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
        sb.append(date + ", ");
        sb.append(subTotal + ", ");
        sb.append(tax + ", ");
        sb.append(total + "");

        Log.d("ObjectFields", "Transaction: " + sb.toString());

    }

    public String getId() { return id.toString(); }
    public void setId(UUID id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public float getSubTotal() { return subTotal; }
    public void setSubTotal(float subTotal) { this.subTotal = subTotal; }
    public float getTax() { return tax; }
    public void setTax(float tax) { this.tax = tax; }
    public float getTotal() { return total; }
    public void setTotal(float total) { this.total = total; }
}
