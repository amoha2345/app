package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FirebaseFirestore ff;
    private RecyclerView recyclerViewRows;
    private RowAdapter rowAdapter;

    // Define your predefined rows here
    private List<Row> predefinedRows;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ff = FirebaseFirestore.getInstance();
        recyclerViewRows = view.findViewById(R.id.recycler_view_rows);
        recyclerViewRows.setLayoutManager(new LinearLayoutManager(getContext()));
        rowAdapter = new RowAdapter();
        recyclerViewRows.setAdapter(rowAdapter);

        // Initialize your predefined rows
        initializePredefinedRows();

        loadDataFromFirestore();

        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> showAddItemDialog());

        return view;
    }

    private void initializePredefinedRows() {
        predefinedRows = new ArrayList<>();
        predefinedRows.add(new Row("Popular in Summer Activities"));
        predefinedRows.add(new Row("Popular in Extracurriculars"));
        predefinedRows.add(new Row("Popular in Community service"));
        predefinedRows.add(new Row("Popular in tutoring"));
        predefinedRows.add(new Row("Popular in research"));
        predefinedRows.add(new Row("Popular in internship"));
    }

    private void loadDataFromFirestore() {
        ff.collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String title = document.getString("title");
                        Item item = new Item(title, document.getString("description"));

                        // Check if the title matches any of the predefined rows
                        boolean added = false;
                        for (Row row : predefinedRows) {
                            if (row.getTitle().equals(title)) {
                                row.addItem(item);
                                added = true;
                                break;
                            }
                        }
                        // Log if item was not added
                        if (!added) {
                            Log.d("HomeFragment", "Item with title " + title + " was not added.");
                        }
                    }
                    displayRows(predefinedRows);
                })
                .addOnFailureListener(e -> Log.w("HomeFragment", "Error loading documents", e));
    }

    private void displayRows(List<Row> rows) {
        rowAdapter.setRows(rows);
        rowAdapter.notifyDataSetChanged();
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Item");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_item, (ViewGroup) getView(), false);

        final EditText inputTitle = viewInflated.findViewById(R.id.input_title);
        final EditText inputDescription = viewInflated.findViewById(R.id.input_description);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            String title = inputTitle.getText().toString();
            String description = inputDescription.getText().toString();
            addNewItemToDatabase(title, description);
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addNewItemToDatabase(String title, String description) {
        Map<String, Object> newItem = new HashMap<>();
        newItem.put("title", title);
        newItem.put("description", description);

        ff.collection("items")
                .add(newItem)
                .addOnSuccessListener(documentReference -> Log.d("HomeFragment", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("HomeFragment", "Error adding document", e));
    }
}
