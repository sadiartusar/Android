package com.example.myapplication.expensetracker.srsadiar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddIncome extends AppCompatActivity {

    private EditText etAmount;
    private Button btnAddIncome;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_income);

        dbHelper = new DatabaseHelper(this);

        etAmount = findViewById(R.id.etAmount);
        btnAddIncome = findViewById(R.id.btnAddIncome);

        btnAddIncome.setOnClickListener(v -> addIncome());
    }

    private void addIncome() {
        String amountStr = etAmount.getText().toString().trim();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add income to database with type "income"
        dbHelper.insertTransaction("Income", amount, "", "General", "income");

        Toast.makeText(this, "Income added successfully", Toast.LENGTH_SHORT).show();
        finish(); // Close activity after adding
    }
}