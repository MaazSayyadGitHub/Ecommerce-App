package com.maaz.ecommerceapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;
import com.maaz.ecommerceapp.Adapters.CartAdapter;
import com.maaz.ecommerceapp.Models.Product;
import com.maaz.ecommerceapp.databinding.ActivityCartBinding;


import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;

    CartAdapter adapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();

        // with the help of tiny cart we can directly add product to cart.
        Cart cart = TinyCartHelper.getCart();

        // it will return map
        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()){
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }

        binding.subtotal.setText(String.format("INR %.2f", cart.getTotalPrice()));

        // sample data
//        products.add(new Product("https://tutorials.mianasad.com/ecommerce/uploads/news/mens%20offers.jpg","Product 1","1", 1233, 23, 1,12));
//        products.add(new Product("https://tutorials.mianasad.com/ecommerce/uploads/news/mens%20offers.jpg","Product 2","2", 1233, 23, 1,12));
//        products.add(new Product("https://tutorials.mianasad.com/ecommerce/uploads/news/mens%20offers.jpg","Product 3","3", 1233, 23, 1,12));


        // understand this, it is very cool to understand interface.
        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                // set subTotal on textView. in activity from adapter.
                binding.subtotal.setText(String.format("INR %.2f", cart.getTotalPrice()));
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // for devider in recyclerview.
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.recyclerview.setLayoutManager(layoutManager);
        binding.recyclerview.addItemDecoration(itemDecoration);

        binding.recyclerview.setAdapter(adapter);

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, CheckOutActivity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}