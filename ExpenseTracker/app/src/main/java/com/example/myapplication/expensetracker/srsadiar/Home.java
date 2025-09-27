package com.example.myapplication.expensetracker.srsadiar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    private TextView tvBalance;
    private Button btnAddIncome, btnAddExpense;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);


        tvBalance = findViewById(R.id.tvBalance);
        btnAddIncome = findViewById(R.id.btnAddIncome);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        dbHelper = new DatabaseHelper(this);

        updateBalance();

        // Add Income button
        btnAddIncome.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, AddIncome.class);
            startActivity(intent);
        });

        // Add Expense button
        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, ExpenseMain.class);
            startActivity(intent);
        });


        // Home.java er onCreate method er modhhe

        Button btnMonthlyReport = findViewById(R.id.btnMonthlyReport); // ekhane button add korbo xml te age

        btnMonthlyReport.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, MonthlyReport.class);
            startActivity(intent);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBalance();
    }

    private void updateBalance() {
        double totalIncome = dbHelper.getTotalByType("income");
        double totalExpense = dbHelper.getTotalByType("expense");
        double balance = totalIncome - totalExpense;
        tvBalance.setText("৳ " + balance);
    }
}