package com.example.fitnexa2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {

    private TextView settingsButton;

    private LinearLayout exerciseCard;
    private LinearLayout dietCard;
    private LinearLayout bmiCard;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_page);

        settingsButton = findViewById(R.id.settingsButton);

        exerciseCard = findViewById(R.id.exerciseCard);
        dietCard = findViewById(R.id.dietCard);
        bmiCard = findViewById(R.id.bmiCard);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        BottomNavHelper.setup(
                this,
                bottomNavigation
        );

        bottomNavigation.setSelectedItemId(R.id.nav_home);

        settingsButton.setOnClickListener(v -> showPopupMenu());

        exerciseCard.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomePage.this,
                    BodyPart.class
            );

            intent.putExtra("isDietSelected", false);

            startActivity(intent);
        });

        dietCard.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomePage.this,
                    MainActivity.class
            );

            intent.putExtra("isDietSelected", true);

            startActivity(intent);
        });

        bmiCard.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomePage.this,
                    BMIActivity.class
            );

            startActivity(intent);
        });
    }

    private void showPopupMenu() {

        PopupMenu popupMenu =
                new PopupMenu(
                        HomePage.this,
                        settingsButton
                );

        MenuInflater inflater =
                popupMenu.getMenuInflater();

        inflater.inflate(
                R.menu.option_menu,
                popupMenu.getMenu()
        );

        popupMenu.setOnMenuItemClickListener(
                item -> {

                    int id = item.getItemId();

                    if (id == R.id.menu_profile) {

                        Intent intent =
                                new Intent(
                                        HomePage.this,
                                        UserDetailActivity.class
                                );

                        startActivity(intent);

                        return true;
                    }

                    else if (id == R.id.menu_settings) {

                        Intent intent =
                                new Intent(
                                        HomePage.this,
                                        SettingsActivity.class
                                );

                        startActivity(intent);

                        return true;
                    }

                    else if (id == R.id.menu_wishlist) {
                        Intent intent = new Intent(HomePage.this, WishList.class);
                        startActivity(intent);

                        return true;
                    }

                    else if (id == R.id.menu_about) {

                        Toast.makeText(
                                HomePage.this,
                                "FitNexa - Fitness & Diet App",
                                Toast.LENGTH_LONG
                        ).show();
                        return true;
                    }
                    return false;
                }
        );

        popupMenu.show();
    }
}