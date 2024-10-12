package com.example.hostal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserProfile extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout linearLayoutProfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        db = FirebaseFirestore.getInstance();
        linearLayoutProfiles = findViewById(R.id.linearLayoutProfiles);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, CreateProfileActivity.class);
                startActivity(intent);
            }
        });

        loadUserProfiles();
    }

    private void loadUserProfiles() {
        db.collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String name = documentSnapshot.getString("aname");
                            String email = documentSnapshot.getString("bemail");
                            String phone = documentSnapshot.getString("cphone");
                            String address = documentSnapshot.getString("daddress");
                            String imageUrl = documentSnapshot.getString("eimageUrl");

                            View profileView = getLayoutInflater().inflate(R.layout.profile_item, null);
                            TextView textViewName = profileView.findViewById(R.id.textViewName);
                            TextView textViewEmail = profileView.findViewById(R.id.textViewEmail);
                            TextView textViewPhone = profileView.findViewById(R.id.textViewPhone);
                            TextView textViewAddress = profileView.findViewById(R.id.textViewAddress);
                            ImageView imageViewProfile = profileView.findViewById(R.id.imageViewProfile);

                            textViewName.setText(name);
                            textViewEmail.setText(email);
                            textViewPhone.setText(phone);
                            textViewAddress.setText(address);

                            if (imageUrl != null) {
                                Glide.with(UserProfile.this)
                                        .load(imageUrl)
                                        .into(imageViewProfile);
                            }

                            linearLayoutProfiles.addView(profileView);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfile.this, "Error loading profiles", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
