package com.example.hostal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ApplicationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        Button buttonCreateStudentAccount = findViewById(R.id.button);
        Button buttonremoveStudentAccount = findViewById(R.id.remove);


        buttonCreateStudentAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationActivity.this, Create_Student_Account.class);
                startActivity(intent);
            }
        });
        buttonremoveStudentAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationActivity.this, StudentManagementActivity.class);
                startActivity(intent);
            }
        });


    }
}
