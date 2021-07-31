package com.spacebux.kuberx;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.spacebux.kuberx.model.Wallet;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class AddWalletAmountActivity extends AppCompatActivity {

    private Context context;
    private Wallet wallet;
    private TextInputEditText amount;
    private Button load;
    private Double added = 0d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet_money);
        Checkout.preload(getApplicationContext());
        setReferences();
        configure();
    }

    private void setReferences() {
        context = this;
        amount = findViewById(R.id.amount);
        load = findViewById(R.id.load);
        wallet = (Wallet) getIntent().getSerializableExtra(App.WALLET);
    }

    private void configure() {
        ActivityResultLauncher<Double> launcher = registerForActivityResult(new Contract(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (!result) {
                    App.showToast(context, "Payment failed");
                    return;
                }
                saveMoneyToWallet();
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(amount.getText()).toString().equals("")) {
                    App.showToast(context, "Minimum amount should be Rs. 10");
                    return;
                } else if (Double.parseDouble(amount.getText().toString()) < 10) {
                    App.showToast(context, "Minimum amount should be Rs. 10");
                    return;
                }
                added = Double.parseDouble(amount.getText().toString());
                launcher.launch(Double.parseDouble(amount.getText().toString()));
            }
        });
    }

    private class Contract extends ActivityResultContract<Double, Boolean> {

        @NonNull
        @NotNull
        @Override
        public Intent createIntent(@NonNull @NotNull Context context, Double input) {
            Intent intent = new Intent(context, PaymentActivity.class);
            intent.putExtra(App.AMOUNT, input);
            return intent;
        }

        @Override
        public Boolean parseResult(int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent intent) {
            if (resultCode != RESULT_OK)
                return false;
            if (intent == null)
                return false;
            return intent.getBooleanExtra(App.PAYMENT_SUCCESS, false);
        }
    }

    public void saveMoneyToWallet() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getUid() == null) {
            return;
        }
        db.collection(App.USERS)
                .document(FirebaseAuth.getInstance().getUid())
                .collection(App.WALLETS)
                .document(wallet.getId())
                .update(App.CURRENT, added + wallet.getCurrent());
        finish();
    }
}