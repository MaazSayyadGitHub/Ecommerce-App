package com.maaz.ecommerceapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.maaz.ecommerceapp.Activities.ProductDetailActivity;
import com.maaz.ecommerceapp.Models.Product;
import com.maaz.ecommerceapp.R;
import com.maaz.ecommerceapp.databinding.ItemProductBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products){
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = products.get(position);
        // set image
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.Image);

        // set Name
        holder.binding.Label.setText(product.getName());
        // set price
        // holder.binding.Price.setText(String.valueOf(product.getPrice())); // this is also true but
        // we want to put INR with price
        holder.binding.Price.setText("INR " + product.getPrice()); // its best

        // if we select any product we will go to Product Details Activity.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("image", product.getImage());
                intent.putExtra("desc", product.getName());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("id", product.getId());

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ItemProductBinding binding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
        }
    }
}
