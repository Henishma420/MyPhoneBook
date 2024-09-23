package com.eles.myphonebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myphonebook.db";
    private static final int DATABASE_VERSION = 1;

    private static ContactDatabase instance;

    public static ContactDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new ContactDatabase(context);
        }
        return instance;
    }

    ContactDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contacts (_id INTEGER PRIMARY KEY, name TEXT, phone TEXT, email TEXT, address TEXT, photo BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public void addContact(String _id, String name, String phone, String email, String address, byte[] photo) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("_id",_id);
            values.put("name", name);
            values.put("phone", phone);
            values.put("email", email);
            values.put("address", address);
            values.put("photo", photo);
            db.insert("contacts", null, values);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public Cursor getAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"_id", "name", "phone", "email", "address", "photo"};
        return db.query("contacts", columns, null, null, null, null, null);
    }

    public void editContact(String id, String name, String phone, String email, String address, byte[] photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE contacts SET name = ?, phone = ?, email = ?, address = ?, photo = ? WHERE _id = ?",
                new Object[]{name, phone, email, address, photo, id});
        db.close();
    }

    public void deleteContact(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM contacts WHERE _id = ?", new Object[]{id});
        db.close();
    }

    public Cursor getContactById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"_id", "name", "phone", "email", "address", "photo"};
        return db.query("contacts", columns, "_id = ?", new String[]{id}, null, null, null);
    }
}