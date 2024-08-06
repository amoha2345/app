package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BlankFragment extends Fragment {

    private EditText editText1, editText2, editText3;
    private Button enableInputButton;
    private Button done;
    private FirebaseAuth mAuth;
    private FirebaseFirestore ff;
    private String userId;
    private boolean isEditMode = false;
    private OnProfileUpdatedListener onProfileUpdatedListener; // Interface variable

    // Interface definition
    public interface OnProfileUpdatedListener {
        void onProfileUpdated();
    }

    // Method to set the listener
    public void setOnProfileUpdatedListener(OnProfileUpdatedListener listener) {
        this.onProfileUpdatedListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        enableInputButton = view.findViewById(R.id.enableInputButton);
        done = view.findViewById(R.id.donebutton);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        ff = FirebaseFirestore.getInstance();
        userId = mAuth.getUid();

        enableInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditMode) {
                    enableEditTexts();
                    enableInputButton.setText("Save");
                } else {
                    saveProfileData();
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
                // Notify the listener (ShareFragment)
                if (onProfileUpdatedListener != null) {
                    onProfileUpdatedListener.onProfileUpdated();
                }
                // Switch back to ShareFragment
                getFragmentManager().popBackStack();
            }
        });

        loadProfileData();
    }

    private void enableEditTexts() {
        editText1.setEnabled(true);
        editText2.setEnabled(true);
        editText3.setEnabled(true);
        isEditMode = true;
    }

    private void loadProfileData() {
        if (userId != null) {
            ff.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        editText1.setText(documentSnapshot.getString("fName"));
                        editText2.setText(documentSnapshot.getString("email"));
                        editText3.setText(documentSnapshot.getString("username"));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle the error
                }
            });
        }
    }

    private void saveProfileData() {
        if (userId != null) {
            String name = editText1.getText().toString();
            String email = editText2.getText().toString();
            String phone = editText3.getText().toString();

            ff.collection("users").document(userId).update("fName", name, "email", email, "username", phone)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Data saved successfully
                            isEditMode = false;
                            enableInputButton.setText("Edit");
                            disableEditTexts();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the error
                        }
                    });
        }
    }

    private void disableEditTexts() {
        editText1.setEnabled(false);
        editText2.setEnabled(false);
        editText3.setEnabled(false);
    }
}

