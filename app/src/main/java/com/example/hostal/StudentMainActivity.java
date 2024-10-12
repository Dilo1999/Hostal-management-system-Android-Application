package com.example.hostal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StudentMainActivity extends AppCompatActivity {

    CardView userimagesCard;
    CardView uservideoCard;
    CardView useraudioCard;
    CardView userdocCard;
    CardView userdownloadCard;
    CardView userappCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        userimagesCard = findViewById(R.id.userimagesCard);
        uservideoCard = findViewById(R.id.uservideoCard);
        useraudioCard = findViewById(R.id.useraudioCard);
        userdocCard = findViewById(R.id.userdocCard);
        userdownloadCard = findViewById(R.id.userdownloadCard);
        userappCard = findViewById(R.id.userappCard);

        userimagesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMainActivity.this, UserProfile.class);
                startActivity(intent);
            }
        });

        uservideoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMainActivity.this, EmergencyContact.class);
                startActivity(intent);
            }
        });

        useraudioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMainActivity.this, UserScanIn.class);
                startActivity(intent);
            }
        });

        userdocCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMainActivity.this, UserScanOut.class);
                startActivity(intent);
            }
        });

        userdownloadCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMainActivity.this, UserRequest.class);
                startActivity(intent);
            }
        });

        userappCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentMainActivity.this, UserNoticebord.class);
                startActivity(intent);
            }
        });
    }
}
