package com.example.fitnexa2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>
        implements Filterable {

    private List<Food> foods;
    private List<Food> foodsFull;

    public FoodAdapter(List<Food> foods) {
        this.foods = foods;
        this.foodsFull = new ArrayList<>(foods);
    }

    // =========================
    // VIEW HOLDER
    // =========================

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtCalorie;
        TextView txtServing;
        TextView txtProtein;
        TextView txtFat;
        TextView txtCarbs;
        TextView txtFiber;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtFoodName);
            txtServing = itemView.findViewById(R.id.txtServing);
            txtCalorie = itemView.findViewById(R.id.txtCalories);
            txtFiber = itemView.findViewById(R.id.txtFiber);
            txtProtein = itemView.findViewById(R.id.protein);
            txtCarbs = itemView.findViewById(R.id.carbs);
            txtFat = itemView.findViewById(R.id.fat);
        }
    }

    // =========================
    // CREATE VIEW
    // =========================

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diet_view_holder, parent, false);

        return new FoodViewHolder(view);
    }

    // =========================
    // BIND DATA
    // =========================

    @Override
    public void onBindViewHolder(
            @NonNull FoodViewHolder holder,
            int position) {

        Food food = foods.get(position);

        // Food name
        holder.txtName.setText(food.getName());

        // Serving size
        holder.txtServing.setText(
                "Serving Size: " + food.getServingSize()
        );

        // Calories
        holder.txtCalorie.setText(
                String.valueOf(food.getCalories())
        );

        // Fiber
        holder.txtFiber.setText(
                String.valueOf(food.getFiber()) + " g"
        );

        // Protein
        holder.txtProtein.setText(
                String.valueOf(food.getProtein()) + " g"
        );

        // Carbs
        holder.txtCarbs.setText(
                String.valueOf(food.getCarbs()) + " g"
        );

        // Fat
        holder.txtFat.setText(
                String.valueOf(food.getFat()) + " g"
        );
    }

    // =========================
    // ITEM COUNT
    // =========================

    @Override
    public int getItemCount() {
        return foods.size();
    }

    // =========================
    // SEARCH FILTER
    // =========================

    @Override
    public Filter getFilter() {
        return foodFilter;
    }

    private final Filter foodFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Food> filteredList = new ArrayList<>();

            if (constraint == null ||
                    constraint.toString().trim().isEmpty()) {

                filteredList.addAll(foodsFull);

            } else {

                String filterPattern =
                        constraint.toString()
                                .toLowerCase()
                                .trim();

                for (Food item : foodsFull) {

                    String name = item.getName() == null
                            ? ""
                            : item.getName().toLowerCase();

                    String category = item.getCategory() == null
                            ? ""
                            : item.getCategory().toLowerCase();

                    String servingSize = item.getServingSize() == null
                            ? ""
                            : item.getServingSize().toLowerCase();

                    if (name.contains(filterPattern)
                            || category.contains(filterPattern)
                            || servingSize.contains(filterPattern)) {

                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(
                CharSequence constraint,
                FilterResults results) {

            foods.clear();

            if (results.values != null) {
                foods.addAll((List<Food>) results.values);
            }

            notifyDataSetChanged();
        }
    };



    public void updateData(List<Food> newFoods) {

        foods.clear();
        foods.addAll(newFoods);

        foodsFull.clear();
        foodsFull.addAll(newFoods);

        notifyDataSetChanged();
    }
}