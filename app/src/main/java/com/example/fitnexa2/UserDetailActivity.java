package com.example.fitnexa2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDetailActivity extends AppCompatActivity {

    private EditText name1;
    private EditText age1;
    private EditText gender1;
    private EditText email1;
    private EditText phone1;

    private Button submit;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_detail);

        name1 = findViewById(R.id.name1);
        age1 = findViewById(R.id.age1);
        gender1 = findViewById(R.id.gender1);
        email1 = findViewById(R.id.email1);
        phone1 = findViewById(R.id.phone1);
        submit = findViewById(R.id.submit);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {

            Toast.makeText(
                    UserDetailActivity.this,
                    "Please login first",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    UserDetailActivity.this,
                    LoginActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();

            return;
        }

        String uid = currentUser.getUid();

        userRef = FirebaseDatabase
                .getInstance()
                .getReference("Users")
                .child(uid);

        if (currentUser.getEmail() != null) {
            email1.setText(currentUser.getEmail());
        }

        loadUserData();

        submit.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {

        if (userRef == null) {
            return;
        }

        userRef.get()
                .addOnSuccessListener(snapshot -> {

                    if (!snapshot.exists()) {
                        return;
                    }

                    String name =
                            snapshot.child("name")
                                    .getValue(String.class);

                    String age =
                            snapshot.child("age")
                                    .getValue(String.class);

                    String gender =
                            snapshot.child("gender")
                                    .getValue(String.class);

                    String email =
                            snapshot.child("email")
                                    .getValue(String.class);

                    String phone =
                            snapshot.child("phone")
                                    .getValue(String.class);

                    if (name != null) {
                        name1.setText(name);
                    }

                    if (age != null) {
                        age1.setText(age);
                    }

                    if (gender != null) {
                        gender1.setText(gender);
                    }

                    if (email != null) {
                        email1.setText(email);
                    }

                    if (phone != null) {
                        phone1.setText(phone);
                    }

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            UserDetailActivity.this,
                            "Unable to load profile: "
                                    + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    private void saveUserData() {

        String name =
                name1.getText()
                        .toString()
                        .trim();

        String age =
                age1.getText()
                        .toString()
                        .trim();

        String gender =
                gender1.getText()
                        .toString()
                        .trim();

        String email =
                email1.getText()
                        .toString()
                        .trim();

        String phone =
                phone1.getText()
                        .toString()
                        .trim();

        if (TextUtils.isEmpty(name)) {

            name1.setError("Enter your name");
            name1.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(age)) {

            age1.setError("Enter your age");
            age1.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(gender)) {

            gender1.setError("Enter your gender");
            gender1.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(phone)) {

            phone1.setError("Enter your phone number");
            phone1.requestFocus();

            return;
        }

        FirebaseUser currentUser =
                mAuth.getCurrentUser();

        if (currentUser == null) {

            Toast.makeText(
                    UserDetailActivity.this,
                    "Session expired. Please login again.",
                    Toast.LENGTH_LONG
            ).show();

            Intent intent = new Intent(
                    UserDetailActivity.this,
                    LoginActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();

            return;
        }

        if (userRef == null) {

            Toast.makeText(
                    UserDetailActivity.this,
                    "Database connection error",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        UserModel userModel =
                new UserModel(
                        name,
                        age,
                        gender,
                        email,
                        phone
                );

        submit.setEnabled(false);
        submit.setText("Saving...");

        userRef.setValue(userModel)
                .addOnSuccessListener(unused -> {

                    SharedPreferences preferences =
                            getSharedPreferences(
                                    "UserPrefs",
                                    MODE_PRIVATE
                            );

                    preferences.edit()
                            .putBoolean("detailsCompleted", true)
                            .apply();

                    Toast.makeText(
                            UserDetailActivity.this,
                            "Profile saved successfully!",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(
                            UserDetailActivity.this,
                            HomePage.class
                    );

                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );

                    startActivity(intent);

                    finish();
                })
                .addOnFailureListener(e -> {

                    submit.setEnabled(true);
                    submit.setText("SAVE & CONTINUE");

                    String errorMessage =
                            e.getMessage();

                    if (errorMessage == null ||
                            errorMessage.isEmpty()) {

                        errorMessage =
                                "Unable to save profile";
                    }

                    Toast.makeText(
                            UserDetailActivity.this,
                            errorMessage,
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}