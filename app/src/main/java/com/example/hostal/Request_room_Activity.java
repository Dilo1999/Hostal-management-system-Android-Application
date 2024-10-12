package com.example.hostal;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Request_room_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore firestore;
    private List<Item> itemList;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_room);

        recyclerView = findViewById(R.id.recyclerViewRequest);
        firestore = FirebaseFirestore.getInstance();
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList, null); // No click listener needed

        // Set GridLayoutManager with 3 columns
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(itemAdapter);

        loadItemsFromFirestore();
    }

    private void loadItemsFromFirestore() {
        firestore.collection("items").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                itemList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Item item = document.toObject(Item.class);
                    item.setId(document.getId());  // Set the Firestore document ID
                    itemList.add(item);
                }
                itemAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(Request_room_Activity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
