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

    public static String NAME_COLUMN_COMPANY_ID = "company_id";
    public static String TYPE_COLUMN_COMPANY_ID = "text";

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
        sb.append(NAME_COLUMN_COMPANY_ID + " " + TYPE_COLUMN_COMPANY_ID + ", ");
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
    public static ArrayList<Inventory> getAllRecords(String companyID){
        SQLiteDatabase db = DBHelper.Instance().getDB();

        ArrayList<Inventory> tempArray = new ArrayList<>();

        String sql = "SELECT Inventory.id as inventory_id, Inventory.company_id as companyId, Inventory.amount, Product.*, ProductImage.imagePath, ProductImage.id as image_id " +
                     "FROM Inventory " +
                     "INNER JOIN Product ON Inventory.product_id = Product.id " +
                     "LEFT JOIN ProductImage ON Inventory.product_id = ProductImage.product_id " +
                     "WHERE Inventory.company_id = '" + companyID + "' AND Inventory.amount > 0 GROUP BY Inventory.id ORDER BY Product.name";

        //Cursor c = db.rawQuery("Select "+ TABLE_NAME +".*, " + Product.TABLE_NAME +".* from " + TABLE_NAME + " where " + NAME_COLUMN_AMOUNT + "> 0", null);
        Cursor c = db.rawQuery(sql, null);
        //logCursor(c);
        while(c.moveToNext()){
            Inventory item = new Inventory(c.getString(c.getColumnIndex("companyId")));
            item.setId(UUID.fromString(c.getString(c.getColumnIndex("inventory_id"))));
            item.setProduct_id(UUID.fromString(c.getString(c.getColumnIndex("id"))));
            item.setAmount(c.getInt(c.getColumnIndex("amount")));

            Product product = new Product(companyID);
            product.setId(c.getString(c.getColumnIndex("id")));
            product.setName(c.getString(c.getColumnIndex("name")));
            product.setUpc(c.getString(c.getColumnIndex("upc")));
            product.setNumber(c.getString(c.getColumnIndex("number")));
            product.setPrice(c.getDouble(c.getColumnIndex("price")));
            product.setDescription(c.getString(c.getColumnIndex("description")));

            String imagePath = c.getString(c.getColumnIndex("imagePath"));

            if(imagePath != null){
                ProductImage image = new ProductImage();
                image.setId(UUID.fromString(c.getString(c.getColumnIndex("image_id"))));
                image.setImagePath(imagePath);
                product.addImage(image);
            }

            item.setProduct(product);


            tempArray.add(item);
        }
        c.close();

        return tempArray;
    }

    //Get item by id
    public static Inventory get(String upc, String companyID){
        SQLiteDatabase db = DBHelper.Instance().getDB();

        String sql = "SELECT Inventory.id as inventory_id, Inventory.company_id as companyId, Inventory.amount, Product.*, ProductImage.imagePath, ProductImage.id as image_id  " +
                     "FROM Inventory " +
                     "INNER JOIN Product ON Inventory.product_id = Product.id " +
                     "LEFT JOIN ProductImage ON Inventory.product_id = ProductImage.product_id " +
                     "WHERE Product.upc = '" + upc + "' AND Inventory.company_id = '" + companyID + "' GROUP BY Inventory.id";

        //Cursor c = db.rawQuery("Select * from " + TABLE_NAME + " where " + NAME_COLUMN_ID + "= '" + id + "'", null);
        Cursor c = db.rawQuery(sql, null);
        //Log.d("dddd", c.getCount() + "");
        //logCursor(c);
        if(c.getCount() != 0) {
            c.moveToFirst();

            Inventory item = new Inventory(c.getString(c.getColumnIndex("companyId")));
            item.setId(UUID.fromString(c.getString(c.getColumnIndex("inventory_id"))));
            item.setProduct_id(UUID.fromString(c.getString(c.getColumnIndex("id"))));
            item.setAmount(c.getInt(c.getColumnIndex("amount")));

            Product product = new Product(companyID);
            product.setId(c.getString(c.getColumnIndex("id")));
            product.setName(c.getString(c.getColumnIndex("name")));
            product.setUpc(c.getString(c.getColumnIndex("upc")));
            product.setNumber(c.getString(c.getColumnIndex("number")));
            product.setPrice(c.getDouble(c.getColumnIndex("price")));
            product.setDescription(c.getString(c.getColumnIndex("description")));

            String imagePath = c.getString(c.getColumnIndex("imagePath"));

            if(imagePath != null){
                ProductImage image = new ProductImage();
                image.setId(UUID.fromString(c.getString(c.getColumnIndex("image_id"))));
                image.setImagePath(imagePath);
                product.addImage(image);
            }

            item.setProduct(product);

            c.close();
            return item;
        }else{
            return null;
        }
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
