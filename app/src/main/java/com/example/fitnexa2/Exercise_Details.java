package com.example.fitnexa2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Exercise_Details extends AppCompatActivity {

    ImageView imgviewexercise2;

    TextView exercisename2;
    TextView exerciselevel;
    TextView force;
    TextView mechanic;
    TextView equipment;
    TextView primarymuscles;
    TextView secondarymusles;
    TextView instructions;

    Button wishlist;
    Button doit;

    Exercise exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exercise_detail);

        imgviewexercise2 = findViewById(R.id.imgviewexercise2);

        exercisename2 = findViewById(R.id.exercisename2);
        exerciselevel = findViewById(R.id.exerciselevel);
        force = findViewById(R.id.force);
        mechanic = findViewById(R.id.mechanic);
        equipment = findViewById(R.id.equipment);
        primarymuscles = findViewById(R.id.primarymuscles);
        secondarymusles = findViewById(R.id.secondarymusles);
        instructions = findViewById(R.id.instructions);

        wishlist = findViewById(R.id.wishlist);
        doit = findViewById(R.id.doit);

        exercise = (Exercise) getIntent()
                .getSerializableExtra("exercise");

        if (exercise == null) {
            finish();
            return;
        }

        displayExercise();

        updateWishlistButton();

        wishlist.setOnClickListener(v -> {

            if (isInWishlist()) {
                removeFromWishlist();
            } else {
                addToWishlist();
            }

        });

        doit.setOnClickListener(v -> {

            Intent intent = new Intent(
                    Exercise_Details.this,
                    StartExercise.class
            );

            intent.putExtra("exercise", exercise);

            startActivity(intent);
        });
    }

    private void displayExercise() {

        exercisename2.setText(
                exercise.getName()
        );

        exerciselevel.setText(
                "Level: " + exercise.getLevel()
        );

        force.setText(
                "Force: " + exercise.getForce()
        );

        mechanic.setText(
                "Mechanic: " + exercise.getMechanic()
        );

        equipment.setText(
                "Equipment: " + exercise.getEquipment()
        );

        if (exercise.getPrimaryMuscles() != null &&
                !exercise.getPrimaryMuscles().isEmpty()) {

            primarymuscles.setText(
                    "Primary Muscles: " +
                            String.join(
                                    ", ",
                                    exercise.getPrimaryMuscles()
                            )
            );
        }

        if (exercise.getSecondaryMuscles() != null &&
                !exercise.getSecondaryMuscles().isEmpty()) {

            secondarymusles.setText(
                    "Secondary Muscles: " +
                            String.join(
                                    ", ",
                                    exercise.getSecondaryMuscles()
                            )
            );
        }

        if (exercise.getInstructions() != null &&
                !exercise.getInstructions().isEmpty()) {

            StringBuilder instructionText =
                    new StringBuilder();

            for (int i = 0;
                 i < exercise.getInstructions().size();
                 i++) {

                instructionText
                        .append(i + 1)
                        .append(". ")
                        .append(
                                exercise.getInstructions().get(i)
                        )
                        .append("\n\n");
            }

            instructions.setText(
                    instructionText.toString()
            );
        }

        if (exercise.getImages() != null &&
                !exercise.getImages().isEmpty()) {

            String imageURL =
                    "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/"
                            + exercise.getImages().get(0);

            Glide.with(this)
                    .load(imageURL)
                    .placeholder(
                            R.drawable.ic_launcher_background
                    )
                    .into(imgviewexercise2);
        }
    }

    private void addToWishlist() {

        List<Exercise> wishlistList =
                getWishlist();

        if (!isInWishlist()) {

            wishlistList.add(exercise);

            saveWishlist(wishlistList);

            Toast.makeText(
                    this,
                    "Added to Wishlist ❤️",
                    Toast.LENGTH_SHORT
            ).show();

            updateWishlistButton();
        }
    }

    private void removeFromWishlist() {

        List<Exercise> wishlistList =
                getWishlist();

        for (int i = 0;
             i < wishlistList.size();
             i++) {

            Exercise item =
                    wishlistList.get(i);

            if (item != null &&
                    item.getName() != null &&
                    item.getName().equalsIgnoreCase(
                            exercise.getName()
                    )) {

                wishlistList.remove(i);
                break;
            }
        }

        saveWishlist(wishlistList);

        Toast.makeText(
                this,
                "Removed from Wishlist",
                Toast.LENGTH_SHORT
        ).show();

        updateWishlistButton();
    }

    private boolean isInWishlist() {

        if (exercise == null ||
                exercise.getName() == null) {

            return false;
        }

        List<Exercise> wishlistList =
                getWishlist();

        for (Exercise item : wishlistList) {

            if (item != null &&
                    item.getName() != null &&
                    item.getName().equalsIgnoreCase(
                            exercise.getName()
                    )) {

                return true;
            }
        }

        return false;
    }

    private void updateWishlistButton() {

        if (isInWishlist()) {

            wishlist.setText(
                    "♥ Remove from Wishlist"
            );

        } else {

            wishlist.setText(
                    "♥ Add to Wishlist"
            );
        }
    }

    private List<Exercise> getWishlist() {

        SharedPreferences preferences =
                getSharedPreferences(
                        "FitNexaWishlist",
                        MODE_PRIVATE
                );

        String json =
                preferences.getString(
                        "wishlist",
                        null
                );

        if (json == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();

        Type type =
                new TypeToken<List<Exercise>>() {}.getType();

        List<Exercise> list =
                gson.fromJson(
                        json,
                        type
                );

        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }

    private void saveWishlist(
            List<Exercise> wishlistList) {

        SharedPreferences preferences =
                getSharedPreferences(
                        "FitNexaWishlist",
                        MODE_PRIVATE
                );

        Gson gson = new Gson();

        String json =
                gson.toJson(wishlistList);

        preferences.edit()
                .putString(
                        "wishlist",
                        json
                )
                .apply();
    }
}