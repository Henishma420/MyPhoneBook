// ContactManager.java
package com.eles.myphonebook;

import android.content.Context;
import android.database.Cursor;
import android.net .Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactManager {
    private ContactDatabase db;
    private Context context;

    public ContactManager(Context context) {
        this.context = context;
        db = ContactDatabase.getInstance(context);
    }

    public void addContact(Contact contact, Uri photoUri) {
        byte[] photoBytes = uriToByteArray(context, photoUri);
        db.addContact(contact.getId(), contact.getName(), contact.getPhone(), contact.getEmail(), contact.getAddress(), photoBytes);
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = db.getAllContacts();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow("photo"));
            Contact contact = new Contact(id, name, phone, email, address, photo);
            contacts.add(contact);
        }
        cursor.close();
        return contacts;
    }

    public void updateContact(Contact contact, Uri photoUri) {
        byte[] photoBytes = uriToByteArray(context, photoUri);
        if (photoBytes != null && !Arrays.equals(contact.getPhotoBytes(), photoBytes)) {
            db.editContact(contact.getId(), contact.getName(), contact.getPhone(), contact.getEmail(), contact.getAddress(), photoBytes);
        }
    }

    private byte[] uriToByteArray(Context context, Uri uri) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null; // or throw a custom exception
        }
        return outputStream.toByteArray();
    }

    public void deleteContact(Contact contact) {
        db.deleteContact(contact.getId());
    }

    public Contact getContactById(String id) {
        Cursor cursor = db.getContactById(id);
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow("photo"));
            Contact contact = new Contact(id, name, phone, email, address, photo);
            cursor.close();
            return contact;
        }
        cursor.close();
        return null;
    }
}