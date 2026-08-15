package com.example.fitnexa2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

public class StartExercise extends AppCompatActivity {

    ImageView exerciseImage;
    TextView exerciseName;
    TextView statusText;
    TextView repsText;
    MaterialButton startButton;

    Exercise exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_exercise);

        exerciseImage = findViewById(R.id.exerciseImage);
        exerciseName = findViewById(R.id.exerciseName);
        statusText = findViewById(R.id.statusText);
        repsText = findViewById(R.id.repsText);
        startButton = findViewById(R.id.startButton);

        exercise = (Exercise) getIntent()
                .getSerializableExtra("exercise");

        if (exercise == null) {
            Toast.makeText(
                    this,
                    "Exercise data not received",
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        displayExercise();

        startButton.setOnClickListener(v -> {

            if (startButton.getText().toString().equals("START")) {

                startButton.setText("DONE");
                statusText.setText("WORKOUT");

            } else {

                finish();
            }
        });
    }

    private void displayExercise() {

        if (exercise.getName() != null) {
            exerciseName.setText(exercise.getName());
        }

        repsText.setText("12 REPS");

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
                    .into(exerciseImage);
        }
    }
}