package com.example.fitnexa2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout layoutEditProfile;
    LinearLayout layoutLogout;
    LinearLayout layoutTerms;
    LinearLayout layoutAbout;
    LinearLayout layoutFeedback;

    Switch switchTheme;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.setting_page
        );

        // Firebase
        mAuth =
                FirebaseAuth.getInstance();

        // Find views
        layoutEditProfile =
                findViewById(
                        R.id.layoutEditProfile
                );

        layoutLogout =
                findViewById(
                        R.id.layoutLogout
                );

        layoutTerms =
                findViewById(
                        R.id.layoutTerms
                );

        layoutAbout =
                findViewById(
                        R.id.layoutAbout
                );

        layoutFeedback =
                findViewById(
                        R.id.layoutFeedback
                );



        layoutEditProfile.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    SettingsActivity.this,
                                    UserDetailActivity.class
                            );

                    intent.putExtra(
                            "editMode",
                            true
                    );

                    startActivity(intent);
                }
        );


        layoutLogout.setOnClickListener(
                v -> {

                    // Firebase logout
                    mAuth.signOut();

                    Toast.makeText(
                            SettingsActivity.this,
                            "Logged out successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    // Go to Login
                    Intent intent =
                            new Intent(
                                    SettingsActivity.this,
                                    LoginActivity.class
                            );

                    // Clear Home and Settings
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );

                    startActivity(intent);

                    finish();
                }
        );


        layoutTerms.setOnClickListener(
                v -> {

                    Toast.makeText(
                            SettingsActivity.this,
                            "Terms and Conditions",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );



        layoutAbout.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    SettingsActivity.this,
                                    AboutActivity.class
                            );

                    startActivity(intent);
                }
        );



        layoutFeedback.setOnClickListener(
                v -> {

                    Toast.makeText(
                            SettingsActivity.this,
                            "Feedback feature coming soon",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );
    }
}
