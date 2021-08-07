package com.spacebux.kuberx.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.spacebux.kuberx.App;
import com.spacebux.kuberx.PaymentActivity;
import com.spacebux.kuberx.R;
import com.spacebux.kuberx.model.Wallet;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class EnterWalletAmount extends Fragment {

    private Context context;
    private Wallet wallet;
    private TextInputEditText amount;
    private Button load;
    private Double added = 0d;
    private NavController navController;

    public EnterWalletAmount() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enter_wallet_amount, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Checkout.preload(getContext());
        setReferences(view);
        configure();
    }

    private void setReferences(View view) {
        context = getContext();
        amount = view.findViewById(R.id.amount);
        load = view.findViewById(R.id.load);
        navController = Navigation.findNavController(view);
        if (getArguments() != null) {
            if (getArguments().getParcelable(App.WALLET) != null)
                wallet = getArguments().getParcelable(App.WALLET);
        }
    }

    private void configure() {
        ActivityResultLauncher<Double> launcher = registerForActivityResult(new EnterWalletAmount.Contract(), new ActivityResultCallback<Boolean>() {
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
//                Bundle bundle = new Bundle();
//                bundle.putDouble(App.AMOUNT, added);
//                navController.navigate(R.id.action_enterWalletAmount_to_payment, bundle);
                launcher.launch(added);
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
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.walletHome, true).build();
        navController.navigate(R.id.action_enterWalletAmount_to_walletHome, null, navOptions);
    }
}