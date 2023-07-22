package com.maaz.ecommerceapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;
import com.maaz.ecommerceapp.Adapters.CartAdapter;
import com.maaz.ecommerceapp.Models.Product;
import com.maaz.ecommerceapp.R;
import com.maaz.ecommerceapp.Utilities.Constants;
import com.maaz.ecommerceapp.databinding.ActivityCheckOutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {

    ActivityCheckOutBinding binding;

    CartAdapter adapter;
    ArrayList<Product> products;

    double totalPrice = 0;
    final int tax = 11; // we are applying 11 % tax on each product for sample.

    ProgressDialog progressDialog;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing..");

        products = new ArrayList<>();

        // with the help of tiny cart we can directly add product to cart.
        cart = TinyCartHelper.getCart();

        // it will return map
        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }


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
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);

        binding.cartList.setAdapter(adapter);

        binding.subtotal.setText(String.format("INR %.2f", cart.getTotalPrice()));

        //  Big Decimal will be converted to double value and then will be percentile with tax amount.
        // add tax price to subtotal.
        totalPrice = (cart.getTotalPrice().doubleValue() * tax / 100) + cart.getTotalPrice().doubleValue();
        // set on textView
        binding.total.setText("INR " + totalPrice);

        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processOrder();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void processOrder() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        // we are sending data with json Request not with String request.
        // we need to make Json Object for send data.

        JSONObject productOrder = new JSONObject();
        JSONObject dataObject = new JSONObject();
        try {
            productOrder.put("address", binding.addressBox.getText().toString());
            productOrder.put("buyer", binding.nameBox.getText().toString());
            productOrder.put("comment", binding.commentBox.getText().toString());
            productOrder.put("created_at", Calendar.getInstance().getTimeInMillis());
            productOrder.put("last_update", Calendar.getInstance().getTimeInMillis());
            productOrder.put("date_ship", Calendar.getInstance().getTimeInMillis());
            productOrder.put("email", binding.emailBox.getText().toString());
            productOrder.put("phone", binding.phoneBox.getText().toString());
            productOrder.put("serial", "cab8c1a4e4421a3b");
            productOrder.put("shipping", "");
            productOrder.put("shipping_location", "");
            productOrder.put("shipping_rate", "0.0");
            productOrder.put("status", "WAITING");
            productOrder.put("tax", tax);
            productOrder.put("total_fees", totalPrice);

            // json array for
            JSONArray product_order_details = new JSONArray();

            // it will return map
            for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
                Product product = (Product) item.getKey();
                int quantity = item.getValue();
                product.setQuantity(quantity);

                products.add(product);

                JSONObject productObj = new JSONObject();
                productObj.put("amount", quantity);
                productObj.put("price_item", product.getPrice());
                productObj.put("product_id", product.getId());
                productObj.put("product_name", product.getName());

                product_order_details.put(productObj);
            }

            dataObject.put("product_order", productOrder);
            dataObject.put("product_order_detail", product_order_details);

            Log.e("err", dataObject.toString()); // to check is it perfectly posting or not.

        } catch (JSONException e) {
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL, dataObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(CheckOutActivity.this, "success order", Toast.LENGTH_SHORT).show();

                        String orderCode = response.getJSONObject("data").getString("code");

                        // payment AlertDialog.
                        new AlertDialog.Builder(CheckOutActivity.this)
                                .setTitle("Order Successfull")
                                .setMessage("Your Order Code is: " + orderCode)
                                .setCancelable(false)
                                .setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(CheckOutActivity.this, PaymentActivity.class);
                                        intent.putExtra("code", orderCode);
                                        startActivity(intent);
                                    }
                                }).show();
                    } else {
                        new AlertDialog.Builder(CheckOutActivity.this)
                                .setTitle("Order Failed")
                                .setMessage("Something went wrong Please try again.")
                                .setCancelable(false)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                        Toast.makeText(CheckOutActivity.this, "failed order", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) { // we are sending data through headers with (security code). it is used with POST Method.

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Security", "secure_code");
                return headers;
            }
        };

        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}