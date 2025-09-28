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

    public double getMonthlyTotalByType(String type, int year, int month) {
        SQLiteDatabase db = this.getReadableDatabase();

        String monthStr = (month < 10) ? "0" + month : String.valueOf(month);
        String pattern = "%/" + monthStr + "/" + year;  // e.g. %/09/2025

        Cursor cursor = db.rawQuery(
                "SELECT SUM(amount) FROM " + TABLE_NAME + " WHERE type=? AND date LIKE ?",
                new String[]{type, pattern}
        );

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }


    // Get total expense per category for a given year+month
    public Cursor getExpenseTotalsByCategoryForMonth(int year, int month) {
        SQLiteDatabase db = this.getReadableDatabase();
        String monthStr = (month < 10) ? "0" + month : String.valueOf(month);
        String pattern = "%/" + monthStr + "/" + year;
        String query = "SELECT category, SUM(amount) as total FROM " + TABLE_NAME +
                " WHERE type='expense' AND date LIKE ?" +
                " GROUP BY category ORDER BY total DESC";
        return db.rawQuery(query, new String[]{pattern});
    }

    // Get total income and total expense for a given year+month (two-row cursor)
    public Cursor getIncomeExpenseTotalsForMonth(int year, int month) {
        SQLiteDatabase db = this.getReadableDatabase();
        String monthStr = (month < 10) ? "0" + month : String.valueOf(month);
        String pattern = "%/" + monthStr + "/" + year;
        String query = "SELECT type, SUM(amount) as total FROM " + TABLE_NAME +
                " WHERE date LIKE ? AND (type='income' OR type='expense')" +
                " GROUP BY type";
        return db.rawQuery(query, new String[]{pattern});
    }

    // Optional: monthly totals across multiple months (e.g., last N months)
// returns cursor with columns: year, month, income_total, expense_total
    public Cursor getMonthlyIncomeExpenseForYear(int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        // month substring: substr(date,4,2) as mm, year substr(date,7,4) as yyyy
        String query = "SELECT substr(date,7,4) as yyyy, substr(date,4,2) as mm, " +
                "SUM(CASE WHEN type='income' THEN amount ELSE 0 END) as income_total, " +
                "SUM(CASE WHEN type='expense' THEN amount ELSE 0 END) as expense_total " +
                "FROM " + TABLE_NAME +
                " WHERE substr(date,7,4)=? " +
                " GROUP BY mm ORDER BY mm";
        return db.rawQuery(query, new String[]{String.valueOf(year)});
    }



}
