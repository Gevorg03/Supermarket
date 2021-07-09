package com.example.menu.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.menu.constructors.Product;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperProducts extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "productManager";
    private static final String TABLE_CONTACTS = "products";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_IMG = "img";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_COUNT = "count";

    public DatabaseHelperProducts(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID +
                " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT," + KEY_COUNT  + " TEXT"  +")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
//        values.put(KEY_ADDRESS, product.getAddrees());
        values.put(KEY_COUNT,product.getCount());
//        values.put(KEY_PRICE,product.getPrice());
//        values.put(KEY_IMG,product.getImg());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }



    Product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID, KEY_NAME, KEY_ADDRESS,
                        KEY_COUNT, KEY_PRICE,KEY_IMG}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Product product = new Product(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4));

        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(Integer.parseInt(cursor.getString(0)));
                product.setName(cursor.getString(1));
                product.setCount(cursor.getString(2));

                productList.add(product);
            } while (cursor.moveToNext());
        }
        return productList;
    }

    public int updateContact(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_COUNT, product.getCount());
        return db.update(TABLE_CONTACTS, values, KEY_ID + "=?",
                new String[]{String.valueOf(product.getId())});
    }

    public void deleteContact(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + "=?",
                new String[]{String.valueOf(product.getId())});
        db.close();
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }
    public String getAddrees(int id){
        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_CONTACTS,null,whereclause,whereargs,null,
                null,null);
        if(cursor.moveToFirst()){
            rv = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS));
        }
        return rv;
    }
    public String getCount(int id){
        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_CONTACTS,null,whereclause,whereargs,null,
                null,null);
        if(cursor.moveToFirst()){
            rv = cursor.getString(cursor.getColumnIndex(KEY_COUNT));
        }
        return rv;
    }
    public String getName(int id){
        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_CONTACTS,null,whereclause,whereargs,null,
                null,null);
        if(cursor.moveToFirst()){
            rv = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        }
        return rv;
    }
    public String getPrice(int id){
        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_CONTACTS,null,whereclause,whereargs,null,
                null,null);
        if(cursor.moveToFirst()){
            rv = cursor.getString(cursor.getColumnIndex(KEY_PRICE));
        }
        return rv;
    }
    public String getImg(String name){
        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String whereclause = "NAME=?";
        String[] whereargs = new String[]{String.valueOf(name)};
        Cursor cursor = db.query(TABLE_CONTACTS,null,whereclause,whereargs,null,
                null,null);
        if(cursor.moveToFirst()){
            rv = cursor.getString(cursor.getColumnIndex(KEY_IMG));
        }
        return rv;
    }
    public int getId(String name){
        int rv = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String whereclause = "NAME=?";
        String[] whereargs = new String[]{String.valueOf(name)};
        Cursor cursor = db.query(TABLE_CONTACTS,null,whereclause,whereargs,null,
                null,null);
        if(cursor.moveToFirst()){
            rv = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        }
        return rv;
    }

    public String getNameProduct(long id){
        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_CONTACTS,null,whereclause,whereargs,null,
                null,null);
        if(cursor.moveToFirst()){
            rv = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        }
        return rv;
    }
}
