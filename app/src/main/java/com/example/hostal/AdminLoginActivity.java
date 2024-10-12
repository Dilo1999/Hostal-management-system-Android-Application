package com.example.hostal;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AdminLoginActivity extends AppCompatActivity {

    private EditText adminEmailEditText;
    private EditText adminPasswordEditText;
    private Button adminLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminEmailEditText = findViewById(R.id.Admin_login_email);
        adminPasswordEditText = findViewById(R.id.Admin_login_password);
        adminLoginButton = findViewById(R.id.Admin_login_button);

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = adminEmailEditText.getText().toString().trim();
                String password = adminPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(AdminLoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else if (email.equals("Admin123@gmail.com") && password.equals("123456")) {
                    Intent intent = new Intent(AdminLoginActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}