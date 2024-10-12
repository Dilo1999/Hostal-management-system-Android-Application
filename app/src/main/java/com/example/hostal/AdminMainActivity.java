package com.example.hostal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminMainActivity extends AppCompatActivity {

    CardView imagesCard;
    CardView videoCard;
    CardView audioCard;
    CardView docCard;
    CardView downloadCard;
    CardView appCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        imagesCard = findViewById(R.id.imagesCard);
        videoCard = findViewById(R.id.videoCard);
        audioCard = findViewById(R.id.audioCard);
        docCard = findViewById(R.id.docCard);
        downloadCard = findViewById(R.id.downloadCard);
        appCard = findViewById(R.id.appCard);

        imagesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, RoomActivity.class);
                startActivity(intent);
            }
        });

        videoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, RegisterPersionActivity.class);
                startActivity(intent);
            }
        });

        audioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, InOutActivity.class);
                startActivity(intent);
            }
        });

        docCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, DocumentActivity.class);
                startActivity(intent);
            }
        });

        downloadCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, DownloadActivity.class);
                startActivity(intent);
            }
        });

        appCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, ApplicationActivity.class);
                startActivity(intent);
            }
        });
    }
}
