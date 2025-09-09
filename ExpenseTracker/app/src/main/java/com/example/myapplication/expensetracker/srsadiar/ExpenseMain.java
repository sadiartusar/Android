package com.example.myapplication.expensetracker.srsadiar;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.expensetracker.srsadiar.model.Expense;
import com.example.myapplication.expensetracker.srsadiar.model.ExpenseAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ExpenseMain extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ArrayList<Expense> expenseList;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_main);

        dbHelper = new DatabaseHelper(this);
        expenseList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new ExpenseAdapter(this, expenseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        // ✅ long press listener
        adapter.setOnItemLongClickListener((expense, position) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Transaction")
                    .setMessage("Are you sure you want to delete this transaction?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                       //delete from databage
                        dbHelper.deleteTransactionById(
                                expense.getId()


                        );

                        // remove from list
                        expenseList.remove(position);
                        adapter.notifyItemRemoved(position);

                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AddExpense.class));
        });

        FloatingActionButton fabChart = findViewById(R.id.fabChart);
        fabChart.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ItemExpense.class));
        });

        loadExpenses();

    }

    private void loadExpenses() {
        expenseList.clear();
        Cursor cursor = dbHelper.getExpenses();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                double amount = cursor.getDouble(2);
                String date = cursor.getString(3);
                String category = cursor.getString(4);
                expenseList.add(new Expense(id, title, amount, date, category));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpenses();
    }

}