package com.dev.lakik.expresspos.Model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.InventoryHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.TransactionHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ppash on 08.02.2017.
 */

public class Transaction extends TransactionHelper implements Parcelable {

    private UUID id;
    private UUID company_id;
    private Date date;
    private float subTotal;
    private float tax;
    private float total;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    private ArrayList<TransactionProduct> arrTransProducts;

    public Transaction(String companyID){
        this.id = UUID.randomUUID();
        this.company_id = UUID.fromString(companyID);
        this.date = new Date();
        this.subTotal = 0;
        this.tax = 0;
        this.total = 0;

        arrTransProducts = new ArrayList<>();
    }

    public Transaction(String companyID, Date date, float subTotal, float tax, float total) {
        this.id = UUID.randomUUID();
        this.company_id = UUID.fromString(companyID);
        this.date = date;
        this.subTotal = subTotal;
        this.tax = tax;
        this.total = total;

        arrTransProducts = new ArrayList<>();
    }

    public Transaction(String companyID, String date, float subTotal, float tax, float total) {
        this.id = UUID.randomUUID();
        this.company_id = UUID.fromString(companyID);
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.subTotal = subTotal;
        this.tax = tax;
        this.total = total;

        arrTransProducts = new ArrayList<>();
    }

    public Transaction(UUID id, String companyID, String date, float subTotal, float tax, float total) {
        this.id = UUID.randomUUID();
        this.company_id = UUID.fromString(companyID);
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.subTotal = subTotal;
        this.tax = tax;
        this.total = total;

        arrTransProducts = new ArrayList<>();
    }



    public void loadProducts(String companyID){
        this.arrTransProducts = TransactionProduct.getAllRecordsByID(id.toString(), companyID);
    }

    public void addProduct(Product product){
        TransactionProduct trProduct = null;

        for(TransactionProduct tr : arrTransProducts){
            if(tr.getProductId().equals(product.getId())){
                trProduct = tr;
            }
        }

        if(trProduct == null){
            trProduct = new TransactionProduct(product);
            arrTransProducts.add(trProduct);
        }else{
            trProduct.addAmount();
        }
    }

    public void removeProduct(TransactionProduct product){
        arrTransProducts.remove(product);
    }

    public int getProductsCount(){
        return arrTransProducts.size();
    }

    //Save current item to database if exists updates record
    public void save(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        ContentValues cv = new ContentValues();

        cv.put(NAME_COLUMN_ID, this.id.toString());
        cv.put(NAME_COLUMN_ID, this.company_id.toString());
        cv.put(NAME_COLUMN_DATE, dateFormat.format(this.date));
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
        sb.append(company_id.toString() + ", ");
        sb.append(date + ", ");
        sb.append(subTotal + ", ");
        sb.append(tax + ", ");
        sb.append(total + "");

        Log.d("ObjectFields", "Transaction: " + sb.toString());

    }

    public String getId() { return id.toString(); }
    public void setId(String id) { this.id = UUID.fromString(id); }
    public String getCompanyId() { return company_id.toString(); }
    public void setCompanyId(String companyId) { this.company_id = UUID.fromString(companyId); }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public void setDate(String date) {
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public float getSubTotal() { return subTotal; }
    public void setSubTotal(float subTotal) { this.subTotal = subTotal; }
    public float getTax() { return tax; }
    public void setTax(float tax) { this.tax = tax; }
    public float getTotal() { return total; }
    public void setTotal(float total) { this.total = total; }

    public ArrayList<TransactionProduct> getProducts() {
        return arrTransProducts;
    }

    protected Transaction(Parcel in) {
        id = UUID.fromString(in.readString());
        company_id = UUID.fromString(in.readString());
        try {
            date = dateFormat.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        subTotal = in.readFloat();
        tax = in.readFloat();
        total = in.readFloat();
        arrTransProducts = in.createTypedArrayList(TransactionProduct.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id.toString());
        dest.writeString(company_id.toString());
        dest.writeString(dateFormat.format(this.date));
        dest.writeFloat(subTotal);
        dest.writeFloat(tax);
        dest.writeFloat(total);
        dest.writeTypedList(arrTransProducts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}
