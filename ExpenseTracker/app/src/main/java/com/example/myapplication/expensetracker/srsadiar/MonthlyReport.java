package com.example.myapplication.expensetracker.srsadiar;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.Calendar;

public class MonthlyReport extends AppCompatActivity {

    private TextView tvMonth, tvIncome, tvExpense, tvBalance;
    private TextView tvIncomeValue, tvExpenseValue;
    private Button btnSelectMonth;
    private PieChart pieChart;

    private DatabaseHelper dbHelper;
    private int selectedYear, selectedMonth; // month: 1-12

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);

        // Initialize views
        tvMonth = findViewById(R.id.tvMonth);
        tvIncome = findViewById(R.id.tvIncome);
        tvExpense = findViewById(R.id.tvExpense);
        tvBalance = findViewById(R.id.tvBalance);

        tvIncomeValue = findViewById(R.id.tvIncomeValue);
        tvExpenseValue = findViewById(R.id.tvExpenseValue);

        pieChart = findViewById(R.id.piechart);
        btnSelectMonth = findViewById(R.id.btnSelectMonth);

        dbHelper = new DatabaseHelper(this);

        // Default current month/year
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH) + 1;

        updateMonthText();
        updateReport();

        btnSelectMonth.setOnClickListener(v -> showMonthYearPicker());
    }

    private void showMonthYearPicker() {
        Calendar c = Calendar.getInstance();
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = month + 1;
                    updateMonthText();
                    updateReport();
                },
                selectedYear, selectedMonth - 1, c.get(Calendar.DAY_OF_MONTH)
        );
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

        // PieChart
        pieChart.clearChart();
        double total = income + expense;
        float incomePercent = total > 0 ? (float) (income * 100 / total) : 0;
        float expensePercent = total > 0 ? (float) (expense * 100 / total) : 0;

        tvIncomeValue.setText("Income: ৳" + (int) income + " (" + (int) incomePercent + "%)");
        tvExpenseValue.setText("Expense: ৳" + (int) expense + " (" + (int) expensePercent + "%)");

        if (income > 0)
            pieChart.addPieSlice(new PieModel("Income", (float) income, Color.parseColor("#4CAF50")));
        if (expense > 0)
            pieChart.addPieSlice(new PieModel("Expense", (float) expense, Color.parseColor("#F44336")));

        pieChart.startAnimation();
    }

    private String getMonthName(int month) {
        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return months[month - 1];
    }
}