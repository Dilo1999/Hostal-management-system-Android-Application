package com.example.hostal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class DownloadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "DownloadActivity";

    private Button buttonSelectImage;
    private ImageView imageViewSelected;
    private Uri imageUri;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        buttonSelectImage = findViewById(R.id.button_select_image);
        imageViewSelected = findViewById(R.id.imageView_selected);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("images");
        firestore = FirebaseFirestore.getInstance();

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewSelected.setImageURI(imageUri);
            uploadImageWithRetry(imageUri, 0);
        }
    }

    private void uploadImageWithRetry(Uri imageUri, int retryCount) {
        if (imageUri != null) {
            StorageReference fileRef = storageRef.child(Objects.requireNonNull(imageUri.getLastPathSegment()));

            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                saveImageInfoToFirestore(imageUrl);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to get download URL: ", e);
                                Toast.makeText(DownloadActivity.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }))
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Upload failed: ", e);
                        if (retryCount < 3) {
                            // Exponential backoff retry
                            long delay = (long) Math.pow(2, retryCount) * 1000;
                            new Handler().postDelayed(() -> uploadImageWithRetry(imageUri, retryCount + 1), delay);
                        } else {
                            Toast.makeText(DownloadActivity.this, "Upload failed after retries: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageInfoToFirestore(String imageUrl) {
        ImageInfo imageInfo = new ImageInfo(imageUrl);

        firestore.collection("images")
                .add(imageInfo)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(DownloadActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to save image info: ", e);
                    Toast.makeText(DownloadActivity.this, "Failed to save image info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Model class to store image information
    public static class ImageInfo {
        private String url;

        public ImageInfo() {
            // Default constructor required for Firestore
        }

        public ImageInfo(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
