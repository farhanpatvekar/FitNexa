package com.example.fitnexa2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvSignup;

    FirebaseAuth mAuth;

    SharedPreferences sharedPreferences;
    SharedPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(
                "FitnexaPrefs",
                MODE_PRIVATE
        );

        userPreferences = getSharedPreferences(
                "UserPrefs",
                MODE_PRIVATE
        );

        if (mAuth.getCurrentUser() != null) {

            boolean detailsCompleted =
                    userPreferences.getBoolean(
                            "detailsCompleted",
                            false
                    );

            Intent intent;

            if (detailsCompleted) {

                intent = new Intent(
                        LoginActivity.this,
                        HomePage.class
                );

            } else {

                intent = new Intent(
                        LoginActivity.this,
                        UserDetailActivity.class
                );
            }

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
            return;
        }

        String savedEmail =
                sharedPreferences.getString(
                        "email",
                        ""
                );

        if (!savedEmail.isEmpty()) {
            etEmail.setText(savedEmail);
        }

        btnLogin.setOnClickListener(v -> {

            String email =
                    etEmail.getText()
                            .toString()
                            .trim();

            String password =
                    etPassword.getText()
                            .toString()
                            .trim();

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Enter Email");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Enter Password");
                return;
            }

            mAuth.signInWithEmailAndPassword(
                            email,
                            password
                    )
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            SharedPreferences.Editor editor =
                                    sharedPreferences.edit();

                            editor.putString(
                                    "email",
                                    email
                            );

                            editor.apply();

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                            ).show();

                            boolean detailsCompleted =
                                    userPreferences.getBoolean(
                                            "detailsCompleted",
                                            false
                                    );

                            Intent intent;

                            if (detailsCompleted) {

                                intent = new Intent(
                                        LoginActivity.this,
                                        HomePage.class
                                );

                            } else {

                                intent = new Intent(
                                        LoginActivity.this,
                                        UserDetailActivity.class
                                );
                            }

                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                            );

                            startActivity(intent);
                            finish();

                        } else {

                            String errorMessage =
                                    task.getException() != null
                                            ? task.getException().getMessage()
                                            : "Login failed";

                            Toast.makeText(
                                    LoginActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });

        tvSignup.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    SignupActivity.class
            );

            startActivity(intent);
        });
    }
}