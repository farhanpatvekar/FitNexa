package com.example.fitnexa2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WishList extends AppCompatActivity {

    ImageButton imageButton;

    RecyclerView recyclerView;

    ExerciseAdapter exerciseAdapter;

    List<Exercise> wishlistList =
            new ArrayList<>();

    @Override
    protected void onCreate(
            @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.wishlist);

        initViews();

        // Back button
        imageButton.setOnClickListener(v -> finish());

        // RecyclerView
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadWishlist();
    }

    public void initViews() {

        imageButton =
                findViewById(R.id.btnBack);

        recyclerView =
                findViewById(
                        R.id.favouriteRecyclerView
                );
    }

    private void loadWishlist() {

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

        if (json != null) {

            Gson gson = new Gson();

            Type type =
                    new TypeToken<List<Exercise>>() {}.getType();

            wishlistList =
                    gson.fromJson(
                            json,
                            type
                    );
        }

        if (wishlistList == null) {

            wishlistList =
                    new ArrayList<>();
        }

        exerciseAdapter =
                new ExerciseAdapter(
                        wishlistList
                );

        recyclerView.setAdapter(
                exerciseAdapter
        );
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (recyclerView != null) {
            loadWishlist();
        }
    }
}