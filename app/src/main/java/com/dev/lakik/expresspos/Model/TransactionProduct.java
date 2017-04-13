package com.dev.lakik.expresspos.Model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.TransactionHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.TransactionProductHelper;

import java.util.UUID;

/**
 * Created by ppash on 08.02.2017.
 */

public class TransactionProduct extends TransactionProductHelper {

    private UUID transactionId;
    private UUID productId;
    private double price;
    private int amount;

    private Product product;

    public TransactionProduct(){}

    public TransactionProduct(Product product){
        this.transactionId = UUID.randomUUID();
        this.productId = UUID.fromString(product.getId());
        this.price = product.getPrice();
        this.amount = 1;

        this.product = product;
    }


    public TransactionProduct(String transactionId, String productId, float price, int amount) {
        this.transactionId = UUID.fromString(transactionId);
        this.productId = UUID.fromString(productId);
        this.price = price;
        this.amount = amount;

        this.product = Product.get(productId.toString());
    }

    public void loadProduct(){
        this.product = Product.get(productId.toString());
    }

    //Save current item to database if exists updates record
    public void save(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        ContentValues cv = new ContentValues();

        cv.put(NAME_COLUMN_TRANSACTION_ID, this.transactionId.toString());
        cv.put(NAME_COLUMN_PRODUCT_ID, this.productId.toString());
        cv.put(NAME_COLUMN_PRICE, this.price);
        cv.put(NAME_COLUMN_AMOUNT, this.amount);

        long id = db.replace(TABLE_NAME, null, cv);
        Log.d("DBHelper", "inserted id: " + id);
    }

    public void addAmount(){
        this.amount++;
    }

    public void removeAmount(){
        this.amount--;
    }

    //delete curent item from table
    public void delete(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + NAME_COLUMN_TRANSACTION_ID + " = '" + this.transactionId + "' AND " + NAME_COLUMN_PRODUCT_ID + " = '" + this.productId + "'");
    }

    public void printObject(){
        StringBuilder sb = new StringBuilder();
        sb.append(transactionId.toString() + ", ");
        sb.append(productId.toString() + ", ");
        sb.append(price + ", ");
        sb.append(amount + "");

        Log.d("ObjectFields", "Inventory: " + sb.toString());

        if(product != null) product.printObject();
    }

    public String getTransactionId() { return transactionId.toString(); }
    public void setTransactionId(UUID id) { this.transactionId = id; }
    public String getProductId() { return productId.toString(); }
    public void setProductId(UUID product_id) { this.productId = product_id; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public Product getProduct(){return product;}
}
