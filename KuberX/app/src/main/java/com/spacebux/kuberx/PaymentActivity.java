package com.spacebux.kuberx;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private final Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        startPayment(getIntent().getDoubleExtra(App.AMOUNT, 0));
    }

    private void startPayment(Double amt) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_p5D8NTY7br1DP6");
        checkout.setImage(R.drawable.common_google_signin_btn_icon_dark);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Hitesh Mitruka");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", amt * 100);//pass amount in currency subunits
            options.put("prefill.email", "mitrukahitesh@gmail.com");
            options.put("prefill.contact", "7396650812");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e(App.TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        intent.putExtra(App.PAYMENT_SUCCESS, true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.i(App.TAG, s);
        intent.putExtra(App.PAYMENT_SUCCESS, false);
        setResult(RESULT_OK, intent);
        finish();
    }
}