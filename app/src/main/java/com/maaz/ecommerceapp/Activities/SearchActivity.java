package com.maaz.ecommerceapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.maaz.ecommerceapp.Adapters.ProductAdapter;
import com.maaz.ecommerceapp.Models.Product;
import com.maaz.ecommerceapp.R;
import com.maaz.ecommerceapp.Utilities.Constants;
import com.maaz.ecommerceapp.databinding.ActivitySearchBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;

    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // here we need query for search result.
        String query = getIntent().getStringExtra("query");

        getSupportActionBar().setTitle(query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back icon.

        getProducts(query);

        // products RecyclerView code
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(gridLayoutManager);
        products = new ArrayList<>();

        productAdapter = new ProductAdapter(this, products);
        binding.productList.setAdapter(productAdapter);

    }

    // this is for back function.
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    void getProducts(String query){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCTS_URL + "?q=" + query; //  + "?q" = it will load with that query data only.

        // replaced with lambda functions.
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject mainObject = new JSONObject(response);
                if (mainObject.getString("status").equals("success")){
                    JSONArray productsArray = mainObject.getJSONArray("products");

                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject object = productsArray.getJSONObject(i);

                        Product product = new Product(
                                Constants.PRODUCTS_IMAGE_URL + object.getString("image"),
                                object.getString("name"),
                                object.getString("status"),
                                object.getDouble("price"),
                                object.getDouble("price_discount"),
                                object.getInt("id"),
                                object.getInt("stock")
                        );
                        products.add(product);
                    }

                    productAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });

        queue.add(request);
    }
}