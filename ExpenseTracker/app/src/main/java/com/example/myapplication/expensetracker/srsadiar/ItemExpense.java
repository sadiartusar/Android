package com.example.myapplication.expensetracker.srsadiar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.expensetracker.srsadiar.model.Expense;
import com.example.myapplication.expensetracker.srsadiar.model.ExpenseAdapter;

import java.util.ArrayList;

public class ItemExpense extends AppCompatActivity {

    private TextView textView;

    private DatabaseHelper dbHelper;
    private ArrayList<Expense> expenseList;
    private ExpenseAdapter adapter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.item_expense);

        textView=findViewById(R.id.tvTitle);
        textView=findViewById(R.id.tvAmount);
        textView=findViewById(R.id.tvDate);
        textView=findViewById(R.id.tvCategory);


    }


}