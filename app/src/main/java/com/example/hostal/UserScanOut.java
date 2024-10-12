package com.example.hostal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserScanOut extends AppCompatActivity {

    private Button openCameraButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_scan_in);

        openCameraButton = findViewById(R.id.openCameraButton);
        openCameraButton.setOnClickListener(v -> openScanner());

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("OutData");
    }

    private void openScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.setPrompt("Scan a QR Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String qrCodeContents = result.getContents();
                Toast.makeText(this, "Scanned QR Code: " + qrCodeContents, Toast.LENGTH_SHORT).show();

                // Get the current timestamp
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                // Store the scanned QR code content and timestamp in Firebase Realtime Database
                String id = databaseReference.push().getKey();
                if (id != null) {
                    Map<String, Object> qrData = new HashMap<>();
                    qrData.put("content", qrCodeContents);
                    qrData.put("timestamp", timestamp);

                    databaseReference.child(id).setValue(qrData)
                            .addOnSuccessListener(aVoid -> Toast.makeText(UserScanOut.this, "QR Code saved to database", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(UserScanOut.this, "Failed to save QR code", Toast.LENGTH_SHORT).show());
                }
            } else {
                Toast.makeText(this, "Failed to scan QR code", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
