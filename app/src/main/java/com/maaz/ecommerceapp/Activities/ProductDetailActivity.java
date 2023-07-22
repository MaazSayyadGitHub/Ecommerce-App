package com.maaz.ecommerceapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;
import com.maaz.ecommerceapp.Models.Category;
import com.maaz.ecommerceapp.Models.Product;
import com.maaz.ecommerceapp.R;
import com.maaz.ecommerceapp.Utilities.Constants;
import com.maaz.ecommerceapp.databinding.ActivityProductDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding;
    Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("desc");
        String image = getIntent().getStringExtra("image");
        double price = getIntent().getDoubleExtra("price", 0);
        int id = getIntent().getIntExtra("id", 0);

        Glide.with(this)
                .load(image)
                .into(binding.productImage);

        // we will fetch description of each specific product on its ids.
        getProductDescription(id);

        getSupportActionBar().setTitle(name); // set name on tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for back button

        // pass product to add to cart.
        Cart cart = TinyCartHelper.getCart();
        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.addItem(currentProduct, 1);
                binding.addToCart.setEnabled(false);
                binding.addToCart.setText("Added To Cart");
            }
        });

    }

    void getProductDescription(int id){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCT_DETAILS_URL + id;  // we need id for specific product

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // here we will get response

                //first we will make json object becos we have data in main JSON OBJECT.
                try {

                    // we have just json object in this api, we dont have json array.
                    JSONObject object = new JSONObject(response);
                    // after we have status : success, if we have message success then we will fetch json arrray.
                    if (object.getString("status").equals("success")){
                        JSONObject product = object.getJSONObject("product"); // here array name is categories

                        String description = product.getString("description");
                        binding.productDescription.setText(
                                Html.fromHtml(description)  // if this document in html tags.
                        );

                        // we are putting all server items into our Product Class.
                        currentProduct = new Product(
                                Constants.PRODUCTS_IMAGE_URL + product.getString("image"),
                                product.getString("name"),
                                product.getString("status"),
                                product.getDouble("price"),
                                product.getDouble("price_discount"),
                                product.getInt("id"),
                                product.getInt("stock")
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // here we will get errors if we have.

            }
        });

        queue.add(request);
    }

    // cart menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // cart menu item selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cart){
            startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }



    // for back button
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}