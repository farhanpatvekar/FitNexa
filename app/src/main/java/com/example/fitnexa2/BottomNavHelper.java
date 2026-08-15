package com.example.fitnexa2;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHelper {

    public static void setup(
            Activity activity,
            BottomNavigationView bottomNavigation
    ) {

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {

                if (!(activity instanceof HomePage)) {
                    Intent intent = new Intent(activity, HomePage.class);
                    activity.startActivity(intent);
                }

                return true;

            } else if (id == R.id.nav_exercise) {

                if (!(activity instanceof BodyPart)) {
                    Intent intent = new Intent(activity, BodyPart.class);

                    intent.putExtra("isDietSelected", false);

                    activity.startActivity(intent);
                }

                return true;

            } else if (id == R.id.nav_diet) {

                if (!(activity instanceof MainActivity)) {
                    Intent intent = new Intent(activity, MainActivity.class);

                    intent.putExtra("isDietSelected", true);

                    activity.startActivity(intent);
                }

                return true;

            } else if (id == R.id.nav_bmi) {

                if (!(activity instanceof BMIActivity)) {
                    Intent intent = new Intent(activity, BMIActivity.class);

                    activity.startActivity(intent);
                }

                return true;
            }

            return false;
        });
    }
}