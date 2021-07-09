package com.example.menu.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.menu.constructors.Contact;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperContacts extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 22;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "password";
    /*rivate static final String KEY_PHONE = "phone";
    private static final String KEY_GENDER = "gender";*/

    public DatabaseHelperContacts(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID +
                " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT," + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);
    }



    /*Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID, KEY_NAME, KEY_PASSWORD},
        KEY_ID + "=?",new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4));

        return contact;
    }*/

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPassword(cursor.getString(2));
               /* contact.setPhone_number(cursor.getString(3));
                contact.setGender(cursor.getString(4));*/

                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PASSWORD, contact.getPassword());
        /*values.put(KEY_PHONE, contact.getPhone_number());
        values.put(KEY_GENDER, contact.getGender());*/

        return db.update(TABLE_CONTACTS, values, KEY_ID + "=?",
                new String[]{String.valueOf(contact.getId())});
    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PASSWORD, contact.getPassword());
        /*values.put(KEY_PHONE, contact.getPhone_number());
        values.put(KEY_GENDER, contact.getGender());*/

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + "=?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }
    public String getPassword(long id){
        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_CONTACTS,null,whereclause,whereargs,null,
                null,null);
        if(cursor.moveToFirst()){
            rv = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
        }
        return rv;
    }
    /*public String getPhone(long id){
        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_CONTACTS,null,whereclause,whereargs,null,
                null,null);
        if(cursor.moveToFirst()){
            rv = cursor.getString(cursor.getColumnIndex(KEY_PHONE));
        }
        return rv;
    }*/
    public String getName(long id){
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
    /*public String getGender(long id){
        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_CONTACTS,null,whereclause,whereargs,null,
                null,null);
        if(cursor.moveToFirst()){
            rv = cursor.getString(cursor.getColumnIndex(KEY_GENDER));
        }
        return rv;
    }*/
    public int getId(String pass){
        int rv = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String whereclause = "PASSWORD=?";
        String[] whereargs = new String[]{String.valueOf(pass)};
        Cursor cursor = db.query(TABLE_CONTACTS,null,whereclause,whereargs,null,
                null,null);
        if(cursor.moveToFirst()){
            rv = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        }
        return rv;
    }
}



