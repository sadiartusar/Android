package com.example.myapplication.expensetracker.srsadiar.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.expensetracker.srsadiar.R;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Expense> expenseList;
    private OnItemLongClickListener longClickListener;

    // Constructor
    public ExpenseAdapter(Context context, ArrayList<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    // Interface for long click
    public interface OnItemLongClickListener {
        void onItemLongClick(Expense expense, int position);
    }

    // Setter for long click listener
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.title.setText(expense.getTitle());
        holder.amount.setText("৳ " + expense.getAmount());
        holder.date.setText(expense.getDate());
        holder.category.setText(expense.getCategory());

        // Handle long click
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(expense, position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void removeItem(int position) {
        expenseList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, amount, date, category;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            amount = itemView.findViewById(R.id.tvAmount);
            date = itemView.findViewById(R.id.tvDate);
            category = itemView.findViewById(R.id.tvCategory);
        }
    }
}
