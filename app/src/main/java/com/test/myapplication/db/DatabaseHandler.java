package com.test.myapplication.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.test.myapplication.ActivityDetail;
import com.test.myapplication.MainActivity;
import com.test.myapplication.model.ModelMain;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hotelmanager";
    private static final String TABLE_CONTACTS = "hotels";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMG = "img";
    private static final String KEY_DESCRIPTION = "description";

    public DatabaseHandler(MainActivity mainActivity) {
        super(mainActivity, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHandler(ActivityDetail activityDetail) {
        super(activityDetail, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_IMG + " TEXT," + KEY_DESCRIPTION + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }


    public void insertDetails(List<ModelMain> modelMainList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        for (int i = 0; i < modelMainList.size(); i++) {
            cValues.put(KEY_ID, modelMainList.get(i).id);
            cValues.put(KEY_NAME, modelMainList.get(i).name);
            cValues.put(KEY_IMG, modelMainList.get(i).img);
            cValues.put(KEY_DESCRIPTION, modelMainList.get(i).description);
            db.insert(TABLE_CONTACTS, null, cValues);
        }
        db.close();
    }

    public boolean isTableEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CONTACTS, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }
        return true;
    }


    public ArrayList<ModelMain> listContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);
        ArrayList<ModelMain> storedContacts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String img = cursor.getString(2);
                String description = cursor.getString(3);
                storedContacts.add(new ModelMain(id, name, img, description));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storedContacts;
    }


    public List<ModelMain> readData(int ids) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from hotels where id=" + ids + "", null);
        ArrayList<ModelMain> modelMainArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String img = cursor.getString(2);
                String description = cursor.getString(3);
                modelMainArrayList.add(new ModelMain(id, name, img, description));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return modelMainArrayList;
    }
}
