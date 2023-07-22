package com.maaz.ecommerceapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.maaz.ecommerceapp.R;
import com.maaz.ecommerceapp.Utilities.Constants;
import com.maaz.ecommerceapp.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String code = getIntent().getStringExtra("code");

        binding.webview.setMixedContentAllowed(true);
        binding.webview.loadUrl(Constants.PAYMENT_URL + code);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}