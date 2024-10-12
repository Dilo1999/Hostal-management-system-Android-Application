package com.example.hostal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Create_Student_Account extends AppCompatActivity {

    private EditText emailStudent, passwordStudent;
    private Button createButtonStu;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student_account);

        emailStudent = findViewById(R.id.Student_AC_create);
        passwordStudent = findViewById(R.id.Student_AC_create_password);
        createButtonStu = findViewById(R.id.Student_AC_create_button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        createButtonStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailStudent.getText().toString().trim();
                String password = passwordStudent.getText().toString().trim();

                if (validateInputs(email, password)) {
                    createUser(email, password);
                }
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailStudent.setError("Email is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordStudent.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            passwordStudent.setError("Password must be >= 6 characters");
            return false;
        }

        return true;
    }

    private void createUser(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User created, add data to Firestore
                            addUserToFirestore(email ,password );
                        } else {
                            Toast.makeText(Create_Student_Account.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addUserToFirestore(String email , String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password);

        db.collection("students")
                .document(email)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Create_Student_Account.this, "Student added to Firestore", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Create_Student_Account.this, "Failed to add user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
