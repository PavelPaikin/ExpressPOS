package com.dev.lakik.expresspos.Model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.InventoryHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.ProductImageHelper;

import java.util.UUID;

/**
 * Created by ppash on 08.02.2017.
 */

public class Inventory extends InventoryHelper {

    private UUID id;
    private UUID product_id;
    private int amount;

    private Product product;

    public Inventory(){}

    public Inventory(Product product, int amount) {
        this.id = UUID.randomUUID();
        this.product_id = UUID.fromString(product.getId());
        this.amount = amount;

        this.product = product;
    }

    public Inventory(UUID product_id, int amount) {
        this.id = UUID.randomUUID();
        this.product_id = product_id;
        this.amount = amount;

        this.product = Product.get(product_id.toString());
    }

    public Inventory(UUID id, UUID product_id, int amount) {
        this.id = id;
        this.product_id = product_id;
        this.amount = amount;

        this.product = Product.get(product_id.toString());
    }

    public void loadProduct(){
        this.product = Product.get(product_id.toString());
    }

    //Save current item to database if exists updates record
    public void save(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        ContentValues cv = new ContentValues();

        cv.put(NAME_COLUMN_ID, this.id.toString());
        cv.put(NAME_COLUMN_PRODUCT_ID, this.product_id.toString());
        cv.put(NAME_COLUMN_AMOUNT, this.amount);

        long id = db.replace(TABLE_NAME, null, cv);
        Log.d("DBHelper", "inserted id: " + id);
    }

    //delete curent item from table
    public void delete(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + NAME_COLUMN_PRODUCT_ID + " = '" + this.id + "'");
    }

    public void printObject(){
        StringBuilder sb = new StringBuilder();
        sb.append(id.toString() + ", ");
        sb.append(product_id.toString() + ", ");
        sb.append(amount + "");

        Log.d("ObjectFields", "Inventory: " + sb.toString());

        if(product != null) product.printObject();
    }

    public String getId() { return id.toString(); }
    public void setId(UUID id) { this.id = id; }
    public String getProduct_id() { return product_id.toString(); }
    public void setProduct_id(UUID product_id) { this.product_id = product_id; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}