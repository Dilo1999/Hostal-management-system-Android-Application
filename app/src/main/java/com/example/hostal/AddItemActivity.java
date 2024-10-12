package com.example.hostal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editText;
    private ImageView imageView;
    private Button buttonAdd;
    private String imageUrl;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);
        buttonAdd = findViewById(R.id.buttonAdd);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imageView.setOnClickListener(v -> {
            // Open image picker
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        buttonAdd.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadImageToFirestore(imageUri);
            } else {
                Toast.makeText(AddItemActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageView);
        }
    }

    private void uploadImageToFirestore(Uri imageUri) {
        // Create a unique file name for the image
        String fileName = "images/" + UUID.randomUUID().toString();
        StorageReference fileReference = storageReference.child(fileName);

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL
                    fileReference.getDownloadUrl().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            imageUrl = task.getResult().toString();
                            // Return image URL and text to RoomActivity
                            String text = editText.getText().toString();
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("image_url", imageUrl);
                            resultIntent.putExtra("text", text);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                            Toast.makeText(AddItemActivity.this, "Add data ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddItemActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddItemActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }
}
