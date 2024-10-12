package com.example.hostal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RegisterPersionActivity extends AppCompatActivity {

    private Spinner spinnerEmails;
    private TextView textViewName, textViewEmail, textViewPhone, textViewAddress;
    private ImageView imageViewProfile;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_persion);

        spinnerEmails = findViewById(R.id.spinnerEmails);
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewAddress = findViewById(R.id.textViewAddress);
        imageViewProfile = findViewById(R.id.imageViewProfile);

        db = FirebaseFirestore.getInstance();

        loadEmails();
    }

    private void loadEmails() {
        db.collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> emailList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            emailList.add(documentSnapshot.getId());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterPersionActivity.this, android.R.layout.simple_spinner_item, emailList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerEmails.setAdapter(adapter);

                        spinnerEmails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedEmail = parent.getItemAtPosition(position).toString();
                                loadUserProfile(selectedEmail);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Do nothing
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterPersionActivity.this, "Error loading emails", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadUserProfile(String email) {
        db.collection("users").document(email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getString("aname");
                        String phone = documentSnapshot.getString("cphone");
                        String address = documentSnapshot.getString("daddress");
                        String imageUrl = documentSnapshot.getString("eimageUrl");

                        textViewName.setText(name);
                        textViewEmail.setText(email);
                        textViewPhone.setText(phone);
                        textViewAddress.setText(address);

                        if (imageUrl != null) {
                            Glide.with(RegisterPersionActivity.this)
                                    .load(imageUrl)
                                    .into(imageViewProfile);
                        } else {
                            imageViewProfile.setImageResource(R.drawable.ic_launcher_foreground); // default image
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterPersionActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
