package com.example.sqliteexample.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sqliteexample.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsDB";
    private static final String TABLE_CONTACTS = "contacts";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE =
                "CREATE TABLE " + TABLE_CONTACTS + " ("
                        + KEY_ID + " INTEGER PRIMARY KEY, "
                        + KEY_NAME + " TEXT, "
                        + KEY_PHONE + " TEXT )";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Add new contact
    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        //   db.execSQL("delete from " + TABLE_CONTACTS);

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhone());

        Cursor cursor = null;
        String checkQuery = "SELECT " + KEY_PHONE + " FROM " + TABLE_CONTACTS +
                " WHERE " + KEY_PHONE + "= '" + contact.getPhone() + "'";
        cursor = db.rawQuery(checkQuery, null);

        if (cursor.getCount() == 0) {
            db.insert(TABLE_CONTACTS, null, values);
        }

        db.close();
    }

    public void clearDB(List<Contact> contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CONTACTS);

        db.close();
    }

    // Get a contact
    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_NAME, KEY_PHONE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return contact;
    }

    // Get all contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all rows
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.set_id(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    // Update a contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhone());

        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.get_id())});
    }

    // Delete a contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.execSQL("delete from " + TABLE_CONTACTS + " where " + KEY_PHONE + "='" + contact.getPhone() + "'");
        //      db.delete(TABLE_CONTACTS, KEY_ID + " = ?", new String[] { String.valueOf(contact.get_id())});
        db.close();
    }


    // Get the contacts count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }
}
