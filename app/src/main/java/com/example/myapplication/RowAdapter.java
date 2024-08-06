package com.example.myapplication;
// RowAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.RowViewHolder> {

    private List<Row> rows;

    public void setRows(List<Row> rows) {
        this.rows = rows;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {
        Row row = rows.get(position);
        holder.bind(row);
    }

    @Override
    public int getItemCount() {
        return rows != null ? rows.size() : 0;
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private RecyclerView recyclerViewItems;
        private ItemAdapter itemAdapter;

        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_row_title);
            recyclerViewItems = itemView.findViewById(R.id.recycler_view_items);

            // Initialize RecyclerView for items
            recyclerViewItems.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            itemAdapter = new ItemAdapter();
            recyclerViewItems.setAdapter(itemAdapter);
        }

        public void bind(Row row) {
            textViewTitle.setText(row.getTitle());
            itemAdapter.setItems(row.getItems());
        }
    }
}
