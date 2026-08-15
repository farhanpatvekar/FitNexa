package com.example.fitnexa2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView AppName1;
    SearchView searchView;

    ImageButton imageButton;
    List<Food> foods = new ArrayList<>();
    FoodAdapter foodAdapter;

    String bodyPart;
    String level;

    List<Exercise> exercises = new ArrayList<>();
    ExerciseAdapter exerciseAdapter;

    boolean isDietSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_home_page);

        BottomNavigationView bottomNavigation =
                findViewById(R.id.bottomNavigation);

        BottomNavHelper.setup(
                this,
                bottomNavigation
        );

        bottomNavigation.getMenu()
                .findItem(R.id.nav_exercise)
                .setChecked(true);

        imageButton = findViewById(R.id.imageButton);
        AppName1 = findViewById(R.id.AppName1);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recycleView1);

        bodyPart = getIntent().getStringExtra("bodyPart");
        level = getIntent().getStringExtra("level");

        isDietSelected = getIntent().getBooleanExtra(
                "isDietSelected",
                false
        );


        android.widget.EditText searchText =
                searchView.findViewById(
                        androidx.appcompat.R.id.search_src_text
                );

        if (searchText != null) {
            searchText.setTextColor(
                    android.graphics.Color.BLACK
            );

            searchText.setHintTextColor(
                    android.graphics.Color.GRAY
            );

            searchText.setTextSize(18);
        }

        android.widget.ImageView searchIcon =
                searchView.findViewById(
                        androidx.appcompat.R.id.search_mag_icon
                );

        if (searchIcon != null) {
            searchIcon.setColorFilter(
                    android.graphics.Color.parseColor("#B8FF1A")
            );
        }

        android.widget.ImageView closeButton =
                searchView.findViewById(
                        androidx.appcompat.R.id.search_close_btn
                );

        if (closeButton != null) {
            closeButton.setColorFilter(
                    android.graphics.Color.parseColor("#B8FF1A")
            );
        }

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        if (isDietSelected) {
            initFoodRecyclerView();
        } else {
            initExerciseRecyclerView();
        }

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(
                            String query) {

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(
                            String newText) {

                        if (isDietSelected) {

                            if (foodAdapter != null) {
                                foodAdapter
                                        .getFilter()
                                        .filter(newText);
                            }

                        } else {

                            if (exerciseAdapter != null) {
                                exerciseAdapter
                                        .getFilter()
                                        .filter(newText);
                            }
                        }

                        return true;
                    }
                }
        );
    }

    private void initFoodRecyclerView() {

        foodAdapter = new FoodAdapter(foods);

        recyclerView.setAdapter(foodAdapter);

        FoodService.getInstance()
                .getFoods()
                .enqueue(
                        new Callback<List<Food>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Food>> call,
                                    Response<List<Food>> response) {

                                if (response.isSuccessful()
                                        && response.body() != null) {

                                    List<Food> foodList =
                                            response.body();

                                    foodAdapter.updateData(
                                            foodList
                                    );

                                    android.widget.Toast.makeText(
                                            MainActivity.this,
                                            "Foods: " + foodList.size(),
                                            android.widget.Toast.LENGTH_SHORT
                                    ).show();

                                } else {

                                    android.widget.Toast.makeText(
                                            MainActivity.this,
                                            "Food API failed: "
                                                    + response.code(),
                                            android.widget.Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }

                            @Override
                            public void onFailure(
                                    Call<List<Food>> call,
                                    Throwable t) {

                                t.printStackTrace();

                                android.widget.Toast.makeText(
                                        MainActivity.this,
                                        "Food Error: "
                                                + t.getMessage(),
                                        android.widget.Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }

    private void initExerciseRecyclerView() {

        exerciseAdapter =
                new ExerciseAdapter(exercises);

        recyclerView.setAdapter(exerciseAdapter);

        ExerciseService.getInstance()
                .getExercises()
                .enqueue(
                        new Callback<List<Exercise>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Exercise>> call,
                                    Response<List<Exercise>> response) {

                                if (response.isSuccessful()
                                        && response.body() != null) {

                                    List<Exercise> allExercises =
                                            response.body();

                                    List<Exercise> filteredExercises =
                                            filterExercises(
                                                    allExercises,
                                                    bodyPart,
                                                    level
                                            );

                                    exerciseAdapter.updateData(
                                            filteredExercises
                                    );
                                }
                            }

                            @Override
                            public void onFailure(
                                    Call<List<Exercise>> call,
                                    Throwable t) {

                                t.printStackTrace();
                            }
                        }
                );
    }

    private List<Exercise> filterExercises(
            List<Exercise> exercises,
            String bodyPart,
            String level) {

        List<Exercise> filtered =
                new ArrayList<>();

        if (bodyPart == null || level == null) {
            return filtered;
        }

        for (Exercise exercise : exercises) {

            if (exercise == null
                    || exercise.getPrimaryMuscles() == null
                    || exercise.getLevel() == null) {

                continue;
            }

            boolean muscleMatch = false;

            if (bodyPart.equals("arms")) {

                muscleMatch =
                        exercise.getPrimaryMuscles()
                                .contains("biceps")
                                || exercise.getPrimaryMuscles()
                                .contains("triceps")
                                || exercise.getPrimaryMuscles()
                                .contains("forearms");

            } else if (bodyPart.equals("legs")) {

                muscleMatch =
                        exercise.getPrimaryMuscles()
                                .contains("quadriceps")
                                || exercise.getPrimaryMuscles()
                                .contains("hamstrings")
                                || exercise.getPrimaryMuscles()
                                .contains("calves")
                                || exercise.getPrimaryMuscles()
                                .contains("glutes")
                                || exercise.getPrimaryMuscles()
                                .contains("adductors")
                                || exercise.getPrimaryMuscles()
                                .contains("abductors");

            } else if (bodyPart.equals("chest")) {

                muscleMatch =
                        exercise.getPrimaryMuscles()
                                .contains("chest");

            } else if (bodyPart.equals("abs")) {

                muscleMatch =
                        exercise.getPrimaryMuscles()
                                .contains("abdominals");

            } else if (bodyPart.equals("shoulder_back")) {

                muscleMatch =
                        exercise.getPrimaryMuscles()
                                .contains("deltoids")
                                || exercise.getPrimaryMuscles()
                                .contains("lats")
                                || exercise.getPrimaryMuscles()
                                .contains("traps")
                                || exercise.getPrimaryMuscles()
                                .contains("middle back")
                                || exercise.getPrimaryMuscles()
                                .contains("lower back");
            }

            if (muscleMatch
                    && exercise.getLevel()
                    .equalsIgnoreCase(level)) {

                filtered.add(exercise);
            }
        }

        return filtered;
    }

}