package com.example.pizzaanddesserts.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbConfig extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database_final_H071221076";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "final_H071221076";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_IS_LOGGED_IN = "isLoggedIn";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_INCOME = "income";

    public DbConfig(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_CREATED_AT + " INTEGER,"
                + COLUMN_INCOME + " INTEGER,"
                + COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0)");
    }

    public void insertData(String username, String password, String phone, int income) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_PHONE, phone);
            values.put(COLUMN_INCOME, income);
            long currentTime = System.currentTimeMillis();
            values.put(COLUMN_CREATED_AT, currentTime);
            values.put(COLUMN_IS_LOGGED_IN, 0);
            long result = db.insert(TABLE_NAME, null, values);
            if (result == -1) {
                Log.e("DbConfig", "Insert failed");
            } else {
                Log.d("DbConfig", "Insert successful, ID: " + result);
            }
        } finally {
            db.close();
        }
    }

    public void updateIncome(int id, int income) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_INCOME, income);
            int rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            Log.d("DbConfig", "Rows updated: " + rows);
        } finally {
            db.close();
        }
    }

    public void updateRecord(int id, String phone) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PHONE, phone);
            int rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            Log.d("DbConfig", "Rows updated: " + rows);
        } finally {
            db.close();
        }
    }

    public void deleteRecords(int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int rows = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            Log.d("DbConfig", "Rows deleted: " + rows);
        } finally {
            db.close();
        }
    }

    public int getLoggedInUserId() {
        SQLiteDatabase db = getReadableDatabase();
        int userId = -1;
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID}, COLUMN_IS_LOGGED_IN + " = ?",
                new String[]{"1"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            }
            cursor.close();
        }
        return userId;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getColumnId() {
        return COLUMN_ID;
    }

    public String getColumnUsername() {
        return COLUMN_USERNAME;
    }

    public String getColumnPhone() {
        return COLUMN_PHONE;
    }

    public String getColumnIncome() {
        return COLUMN_INCOME;
    }

    public String getColumnCreatedAt() {
        return COLUMN_CREATED_AT;
    }
}
