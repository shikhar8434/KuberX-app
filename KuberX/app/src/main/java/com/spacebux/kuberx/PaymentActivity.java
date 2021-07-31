package com.spacebux.kuberx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Checkout.preload(getApplicationContext());
        startPayment();
    }

    private void startPayment() {
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
            options.put("amount", 50000);//pass amount in currency subunits
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
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getUid() == null) {
            return;
        }
        db.collection(App.USERS)
                .document(FirebaseAuth.getInstance().getUid())
                .collection(App.WALLETS)
                .document(getIntent().getStringExtra(App.WALLET_ID))
                .update(App.CURRENT, 500 + getIntent().getDoubleExtra(App.CURRENT, 0));
        finish();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.i(App.TAG, s);
        finish();
    }
}