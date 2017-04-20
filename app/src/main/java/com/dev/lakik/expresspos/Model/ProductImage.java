package com.dev.lakik.expresspos.Model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.ProductHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.ProductImageHelper;

import java.util.UUID;

/**
 * Created by ppash on 08.02.2017.
 */

public class ProductImage extends ProductImageHelper implements Parcelable {

    private UUID id;
    private UUID product_id;
    private String imagePath;

    public ProductImage(){
        this.id = UUID.randomUUID();
    }

    public ProductImage(String imagePath) {
        this.id = UUID.randomUUID();
        this.imagePath = imagePath;
    }

    public ProductImage(UUID id, UUID product_id, String imagePath) {
        this.id = id;
        this.product_id = product_id;
        this.imagePath = imagePath;
    }



    //Save current item to database if exists updates record
    public void save(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        ContentValues cv = new ContentValues();

        cv.put(NAME_COLUMN_ID, this.id.toString());
        cv.put(NAME_COLUMN_PRODUCT_ID, this.product_id.toString());
        cv.put(NAME_COLUMN_IMAGE_PATH, this.imagePath);

        long id = db.replace(TABLE_NAME, null, cv);
        Log.d("DBHelper", "inserted id: " + id);
    }

    //delete curent item from table
    public void delete(){
        SQLiteDatabase db = DBHelper.Instance().getDB();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + NAME_COLUMN_ID + " = '" + this.id.toString() + "'");
    }

    public void printObject(){
        StringBuilder sb = new StringBuilder();
        sb.append(product_id.toString() + ", ");
        sb.append(imagePath + "");

        Log.d("ObjectFields", "Image: " + sb.toString());
    }

    public String getId() { return id.toString(); }
    public void setId(UUID id) { this.id = id; }
    public String getProduct_id() { return product_id.toString(); }
    public void setProduct_id(UUID product_id) { this.product_id = product_id; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    protected ProductImage(Parcel in) {
        id = UUID.fromString(in.readString());
        product_id = UUID.fromString(in.readString());
        imagePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id.toString());
        dest.writeString(product_id.toString());
        dest.writeString(imagePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductImage> CREATOR = new Creator<ProductImage>() {
        @Override
        public ProductImage createFromParcel(Parcel in) {
            return new ProductImage(in);
        }

        @Override
        public ProductImage[] newArray(int size) {
            return new ProductImage[size];
        }
    };
}
