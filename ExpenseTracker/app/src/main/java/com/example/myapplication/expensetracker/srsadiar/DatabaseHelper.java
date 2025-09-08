package com.example.myapplication.expensetracker.srsadiar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "expense_db";
    private static final int DB_VERSION = 2; // DB version updated
    private static final String TABLE_NAME = "expenses";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "amount DOUBLE, " +
                "date TEXT, " +
                "category TEXT, " +
                "type TEXT)"; // type column added
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simple upgrade: drop and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert new transaction (expense or income)
    public void insertTransaction(String title, double amount, String date, String category, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("amount", amount);
        values.put("date", date);
        values.put("category", category);
        values.put("type", type); // "expense" or "income"
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Get all transactions
    public Cursor getExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY date DESC", null);
    }

    // Get transactions by type
    public Cursor getIncomes(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE type=? ORDER BY date DESC", new String[]{type});
    }

    // Get total amount by type
    public double getTotalByType(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_NAME + " WHERE type=?", new String[]{type});
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public void deleteTransactionById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

}
