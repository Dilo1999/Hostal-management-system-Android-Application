package com.example.hostal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InOutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QRCodeAdapter qrCodeAdapter;
    private List<QRCodeData> qrCodeList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        qrCodeList = new ArrayList<>();
        qrCodeAdapter = new QRCodeAdapter(qrCodeList);
        recyclerView.setAdapter(qrCodeAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("scannedCodes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                qrCodeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    QRCodeData qrCodeData = snapshot.getValue(QRCodeData.class);
                    qrCodeList.add(qrCodeData);
                }
                qrCodeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
