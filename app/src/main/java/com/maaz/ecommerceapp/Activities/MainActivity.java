package com.maaz.ecommerceapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.maaz.ecommerceapp.Adapters.CategoryAdapter;
import com.maaz.ecommerceapp.Adapters.ProductAdapter;
import com.maaz.ecommerceapp.Models.Category;
import com.maaz.ecommerceapp.Models.Product;
import com.maaz.ecommerceapp.R;
import com.maaz.ecommerceapp.Utilities.Constants;
import com.maaz.ecommerceapp.databinding.ActivityMainBinding;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;

    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", text.toString()); // text of search will be pass
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        addCarousel(); // carousel

        init_Categories(); // categories

        init_Products(); // products

    }

    private void addCarousel() {
        // sample data
//        binding.carousel.addData(new CarouselItem("https://tutorials.mianasad.com/ecommerce/uploads/news/1684909860413.jpg", "Sports Arena"));
//        binding.carousel.addData(new CarouselItem("https://tutorials.mianasad.com/ecommerce/uploads/news/1684666524257.jpg", "Special Offers - All Seasons"));
//        binding.carousel.addData(new CarouselItem("https://tutorials.mianasad.com/ecommerce/uploads/news/mens%20offers.jpg", "Mens Offers"));

        RecentOfferNews();
    }

    void init_Categories(){
        // category RecyclerView code
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        binding.categoriesList.setLayoutManager(gridLayoutManager);
        categories = new ArrayList<>();

        // sample data in arrayList.
//         categories.add(new Category(1, "Sports Outdoors", "https://tutorials.mianasad.com/ecommerce/uploads/category/1684733878508.png", "#4db151", "Description Here"));
//         categories.add(new Category(2, "Sports", "https://tutorials.mianasad.com/ecommerce/uploads/category/1684733878508.png", "#4db151", "Description Here"));
//         categories.add(new Category(3, "Sports Outdoor", "https://tutorials.mianasad.com/ecommerce/uploads/category/1684733878508.png", "#4db151", "Description Here"));
//         categories.add(new Category(4, "Sports outdoor", "https://tutorials.mianasad.com/ecommerce/uploads/category/1684733878508.png", "#4db151", "Description Here"));
//         categories.add(new Category(5, "Sports", "https://tutorials.mianasad.com/ecommerce/uploads/category/1684733878508.png", "#4db151", "Description Here"));
//         categories.add(new Category(6, "Sports outdoor", "https://tutorials.mianasad.com/ecommerce/uploads/category/1684733878508.png", "#4db151", "Description Here"));
//         categories.add(new Category(7, "Sports", "https://tutorials.mianasad.com/ecommerce/uploads/category/1684733878508.png", "#4db151", "Description Here"));

        getCategories();

        categoryAdapter = new CategoryAdapter(this, categories);
        binding.categoriesList.setAdapter(categoryAdapter);
    }

    void getCategories(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_CATEGORIES_URL;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // here we will get response

                //first we will make json object becos we have data in main JSON OBJECT.
                try {
                    Log.i("RESPONSE", response);

                    JSONObject mainObject = new JSONObject(response);
                    // after we have status : success, if we have message success then we will fetch json arrray.
                    if (mainObject.getString("status").equals("success")){
                        JSONArray categoriesArray = mainObject.getJSONArray("categories"); // here array name is categories

                        // run for loop for each array object
                        for (int i = 0; i < categoriesArray.length(); i++) {
                            // under json object
                            JSONObject object = categoriesArray.getJSONObject(i); // put each object on "object"
                            Category category = new Category(
                                    object.getInt("id"),
                                    object.getString("name"),
                                    Constants.CATEGORIES_IMAGE_URL + object.getString("icon"), // in our icon we don't have proper url thats why we adding this before
                                    object.getString("color"),
                                    object.getString("brief")
                            );

                            categories.add(category); // add each category object in list.
                        }

                        categoryAdapter.notifyDataSetChanged(); // here we need to inform that data is changed.
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

    void init_Products(){
        // products RecyclerView code
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(gridLayoutManager);
        products = new ArrayList<>();

        // sample data in arrayList
//        products.add(new Product("https://tutorials.mianasad.com/ecommerce/uploads/product/1682612932070.jpg", "Refrigerator", "Available", 12, 12, 1, 12));
//        products.add(new Product("https://tutorials.mianasad.com/ecommerce/uploads/product/1682612932070.jpg", "Refrigerator", "Available", 12, 12, 1, 12));
//        products.add(new Product("https://tutorials.mianasad.com/ecommerce/uploads/product/1684420745337.jpg", "Refrigerator", "Available", 12, 12, 1, 12));
//        products.add(new Product("https://tutorials.mianasad.com/ecommerce/uploads/product/1684420745337.jpg", "Refrigerator", "Available", 12, 12, 1, 12));
//        products.add(new Product("https://tutorials.mianasad.com/ecommerce/uploads/product/1684420745337.jpg", "Refrigerator", "Available", 12, 12, 1, 12));
//        products.add(new Product("https://tutorials.mianasad.com/ecommerce/uploads/product/1682612932070.jpg", "Refrigerator", "Available", 12, 12, 1, 12));
//        products.add(new Product("https://tutorials.mianasad.com/ecommerce/uploads/product/1684420745337.jpg", "Refrigerator", "Available", 12, 12, 1, 12));
//        products.add(new Product("https://tutorials.mianasad.com/ecommerce/uploads/product/1684420745337.jpg", "Refrigerator", "Available", 12, 12, 1, 12));
//        products.add(new Product("https://tutorials.mianasad.com/ecommerce/uploads/product/1684420745337.jpg", "Refrigerator", "Available", 12, 12, 1, 12));

        getRecentProducts();

        productAdapter = new ProductAdapter(this, products);
        binding.productList.setAdapter(productAdapter);
    }

    void getRecentProducts(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCTS_URL + "?count=8"; //  + "?count=8" = it will load just 8 tems.

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

    void RecentOfferNews(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, response -> {

            try {
                JSONObject mainObject = new JSONObject(response);
                // if status is success then we can get data.
                if (mainObject.getString("status").equals("success")){
                    JSONArray offerArray = mainObject.getJSONArray("news_infos");

                    for (int i = 0; i < offerArray.length(); i++) {
                        JSONObject childObject = offerArray.getJSONObject(i);

                        binding.carousel.addData(
                                new CarouselItem(
                                        Constants.NEWS_IMAGE_URL + childObject.getString("image"),
                                        childObject.getString("title")
                                ));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });

        queue.add(request);
    }
}