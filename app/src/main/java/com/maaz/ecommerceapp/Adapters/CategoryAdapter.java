package com.maaz.ecommerceapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.maaz.ecommerceapp.Activities.CategoryActivity;
import com.maaz.ecommerceapp.Models.Category;
import com.maaz.ecommerceapp.R;
import com.maaz.ecommerceapp.databinding.ItemCategoriesBinding;

import java.net.HttpCookie;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private ArrayList<Category> categories;

    public CategoryAdapter(Context context, ArrayList<Category> categories){
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);

        // load image
        //  holder.binding.image.setImageResource(category.getIcon());  error
        Glide.with(context)
                .load(category.getIcon())
                .into(holder.binding.image);
        // set name
        holder.binding.label.setText(Html.fromHtml(category.getName())); // we can use html tags from server in our app like "br" tag.

        // set background color
        // we need colors in Int but colors are in String.
        // Color.parseColor = convert String to Integer.
        holder.binding.image.setBackgroundColor(Color.parseColor(category.getColor()));

        // pass category id and name for next activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryActivity.class);
            intent.putExtra("catId", category.getId());
            intent.putExtra("categoryName", category.getName());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ItemCategoriesBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoriesBinding.bind(itemView);
        }
    }
}
