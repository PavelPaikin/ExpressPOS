package com.dev.lakik.expresspos.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.dev.lakik.expresspos.Model.Company;
import com.dev.lakik.expresspos.Model.Inventory;
import com.dev.lakik.expresspos.Model.Product;
import com.dev.lakik.expresspos.Model.ProductImage;
import com.dev.lakik.expresspos.Model.Transaction;
import com.dev.lakik.expresspos.Model.TransactionProduct;


public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper db;

    public static int DB_VERSION = 1;
    public static String DB_NAME = "posdb.db";
    SQLiteDatabase oDB;

    public static DBHelper Instance() {
        return db;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        oDB = getWritableDatabase();

        if(db == null){
            db = this;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Company.getCreateQuery());
        db.execSQL(Inventory.getCreateQuery());
        db.execSQL(Product.getCreateQuery());
        db.execSQL(ProductImage.getCreateQuery());
        db.execSQL(Transaction.getCreateQuery());
        db.execSQL(TransactionProduct.getCreateQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getDB(){
        return oDB;
    }
}
