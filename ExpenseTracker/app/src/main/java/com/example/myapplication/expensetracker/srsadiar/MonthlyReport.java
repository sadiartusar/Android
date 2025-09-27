package com.example.myapplication.expensetracker.srsadiar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MonthlyReport extends AppCompatActivity {

    private TextView tvMonth, tvIncome, tvExpense, tvBalance;
    private Button btnSelectMonth;

    private DatabaseHelper dbHelper;

    private int selectedYear, selectedMonth; // month: 1-12

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_monthly_report);
        tvMonth = findViewById(R.id.tvMonth);
        tvIncome = findViewById(R.id.tvIncome);
        tvExpense = findViewById(R.id.tvExpense);
        tvBalance = findViewById(R.id.tvBalance);
        btnSelectMonth = findViewById(R.id.btnSelectMonth);

        dbHelper = new DatabaseHelper(this);

        // Set default to current month/year
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH) + 1;

        updateMonthText();
        updateReport();

        btnSelectMonth.setOnClickListener(v -> showMonthYearPicker());
    }

    private void showMonthYearPicker() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = month + 1;
                    updateMonthText();
                    updateReport();
                },
                selectedYear, selectedMonth - 1, c.get(Calendar.DAY_OF_MONTH)
        );
        // Hide day picker (optional, but Android's native DatePickerDialog does not support month-year only picker)
        datePickerDialog.show();
    }

    private void updateMonthText() {
        String monthName = getMonthName(selectedMonth);
        tvMonth.setText("Report for: " + monthName + " " + selectedYear);
    }

    private void updateReport() {
        double income = dbHelper.getMonthlyTotalByType("income", selectedYear, selectedMonth);
        double expense = dbHelper.getMonthlyTotalByType("expense", selectedYear, selectedMonth);
        double balance = income - expense;

        tvIncome.setText("Income: ৳ " + income);
        tvExpense.setText("Expense: ৳ " + expense);
        tvBalance.setText("Balance: ৳ " + balance);
    }

    private String getMonthName(int month) {
        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return months[month - 1];
    }
}