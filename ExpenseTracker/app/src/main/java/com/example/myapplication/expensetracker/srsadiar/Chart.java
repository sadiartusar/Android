package com.example.myapplication.expensetracker.srsadiar;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


public class Chart extends AppCompatActivity {

    private PieChart pieChart;
    private TextView tvIncomeValue, tvExpenseValue;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart); // তোমার modern XML

        // Initialize Views
        pieChart = findViewById(R.id.piechart);
        tvIncomeValue = findViewById(R.id.tvIncomeValue);
        tvExpenseValue = findViewById(R.id.tvExpenseValue);

        dbHelper = new DatabaseHelper(this);

        loadChartData();
    }

    private void loadChartData() {
        // DB থেকে totals
        double totalIncome = dbHelper.getTotalByType("income");
        double totalExpense = dbHelper.getTotalByType("expense");
        double total = totalIncome + totalExpense;

        // Avoid division by zero
        float incomePercent = total > 0 ? (float) (totalIncome * 100 / total) : 0;
        float expensePercent = total > 0 ? (float) (totalExpense * 100 / total) : 0;

        // Update TextViews
        tvIncomeValue.setText("৳" + (int) totalIncome + " (" + (int) incomePercent + "%)");
        tvExpenseValue.setText("৳" + (int) totalExpense + " (" + (int) expensePercent + "%)");

        // PieChart
        pieChart.clearChart();

        if (totalIncome > 0) {
            pieChart.addPieSlice(new PieModel("Income", (float) totalIncome, Color.parseColor("#4CAF50"))); // Green
        }
        if (totalExpense > 0) {
            pieChart.addPieSlice(new PieModel("Expense", (float) totalExpense, Color.parseColor("#F44336"))); // Red
        }

        pieChart.startAnimation();
    }
}