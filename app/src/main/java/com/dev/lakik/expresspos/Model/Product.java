package com.dev.lakik.expresspos.Model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.CompanyHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.ProductHelper;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by ppash on 08.02.2017.
 */

public class Product extends ProductHelper implements Parcelable {

    private UUID id;
    private String name;
    private String number;
    private String upc;
    private String description;
    private double price;

    private ArrayList<ProductImage> imageArray;

    public Product(){
        this.id = UUID.randomUUID();
        this.imageArray = new ArrayList<>();
    }

    public Product(String name, String number, String upc, String description, double price) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.number = number;
        this.upc = upc;
        this.description = description;
        this.price = price;

        this.imageArray = new ArrayList<>();
    }

    public Product(String id, String name, String number, String upc, String description, double price) {
        this.id = UUID.fromString(id);
        this.name = name;
        this.number = number;
        this.upc = upc;
        this.description = description;
        this.price = price;

        //this.imageArray = ProductImage.getAllRecordsForProduct(id);
    }

    public void loadImages(){
        this.imageArray = ProductImage.getAllRecordsForProduct(this.id.toString());
    }

    //Save current item to database if exists updates record
    public void save(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        ContentValues cv = new ContentValues();
        
        cv.put(NAME_COLUMN_ID, this.id.toString());
        cv.put(NAME_COLUMN_NAME, this.name);
        cv.put(NAME_COLUMN_NUMBER, this.number);
        cv.put(NAME_COLUMN_UPC, this.upc);
        cv.put(NAME_COLUMN_DESCRIPTION, this.description);
        cv.put(NAME_COLUMN_PRICE, this.price);

        long id = db.replace(TABLE_NAME, null, cv);
        Log.d("DBHelper", "inserted id: " + id);

        for(ProductImage image : imageArray){
            image.save();
        }
    }

    //delete curent item from table
    public void delete(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + NAME_COLUMN_ID + " = '" + this.id + "'");

        for(ProductImage image : imageArray){
            image.delete();
        }
    }

    public void addImage(ProductImage image){
        image.setProduct_id(id);
        //image.save();
        imageArray.add(image);
    }

    public void removeImage(ProductImage image){
        imageArray.remove(image);
        image.delete();
    }

    public void printObject(){
        StringBuilder sb = new StringBuilder();
        sb.append(id.toString() + ", ");
        sb.append(name + ", ");
        sb.append(number + ", ");
        sb.append(upc + ", ");
        sb.append(description + ", ");
        sb.append(price + "");

        Log.d("ObjectFields", "Product: " + sb.toString());

        for(ProductImage image : imageArray){
            image.printObject();
        }
    }

    public String getId() { return id.toString(); }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getUpc() { return upc; }
    public void setUpc(String upc) { this.upc = upc; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public ArrayList<ProductImage> getImages(){ return imageArray; }


    protected Product(Parcel in) {
        id = UUID.fromString(in.readString());
        name = in.readString();
        number = in.readString();
        upc = in.readString();
        description = in.readString();
        price = in.readDouble();

        loadImages();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id.toString());
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(upc);
        dest.writeString(description);
        dest.writeDouble(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

}
