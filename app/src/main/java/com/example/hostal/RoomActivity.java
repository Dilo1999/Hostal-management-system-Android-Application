package com.example.hostal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity implements ItemAdapter.OnItemLongClickListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private FirebaseFirestore firestore;
    private List<Item> itemList;
    private ItemAdapter itemAdapter;

    private static final int REQUEST_CODE_ADD_ITEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab123);
        firestore = FirebaseFirestore.getInstance();
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList, this);

        // Set GridLayoutManager with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(itemAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an activity for result to get image and text input
                Intent intent = new Intent(RoomActivity.this, AddItemActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_ITEM);
            }
        });

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
                Toast.makeText(RoomActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ITEM && resultCode == RESULT_OK) {
            if (data != null) {
                String imageUrl = data.getStringExtra("image_url");
                String text = data.getStringExtra("text");
                addItemToFirestore(new Item(imageUrl, text));
            }
        }
    }

    private void addItemToFirestore(Item item) {
        firestore.collection("items").add(item).addOnSuccessListener(documentReference -> {
            item.setId(documentReference.getId());  // Set the Firestore document ID
            itemList.add(item);
            itemAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(RoomActivity.this, "Error adding document", Toast.LENGTH_SHORT).show();
        });
    }

    private void deleteItemFromFirestore(Item item) {
        firestore.collection("items").document(item.getId()).delete().addOnSuccessListener(aVoid -> {
            itemList.remove(item);
            itemAdapter.notifyDataSetChanged();
            Toast.makeText(RoomActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(RoomActivity.this, "Error deleting document", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onItemLongClick(int position) {
        Item item = itemList.get(position);
        deleteItemFromFirestore(item);
    }
}
