package com.spacebux.kuberx.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
import com.spacebux.kuberx.App;
import com.spacebux.kuberx.R;
import com.spacebux.kuberx.model.Wallet;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AddWallet extends Fragment {

    private Context context;
    private TextInputEditText title, amount, weeks;
    private Button save;
    private NavController navController;

    public AddWallet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_wallet, container, false);
        setReferences(view);
        configure();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void configure() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wallet wallet = new Wallet();
                if (Objects.requireNonNull(title.getText()).toString().equals("")) {
                    App.showToast(context, "Title is required");
                    title.requestFocus();
                    return;
                }
                wallet.setTitle(title.getText().toString());
                if (Objects.requireNonNull(amount.getText()).toString().equals("")) {
                    App.showToast(context, "Amount is required");
                    amount.requestFocus();
                    return;
                }
                wallet.setTarget(Double.valueOf(amount.getText().toString()));
                if (Objects.requireNonNull(weeks.getText()).toString().equals("")) {
                    App.showToast(context, "Weeks is required");
                    weeks.requestFocus();
                    return;
                }
                wallet.setWeeks(Double.valueOf(weeks.getText().toString()));
                Bundle bundle = new Bundle();
                bundle.putParcelable(App.WALLET, wallet);
                NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.walletHome, true).build();
                navController.navigate(R.id.action_addWallet_to_walletHome, bundle, options);
            }
        });
    }

    private void setReferences(View view) {
        context = getContext();
        title = view.findViewById(R.id.title);
        amount = view.findViewById(R.id.amount);
        weeks = view.findViewById(R.id.weeks);
        save = view.findViewById(R.id.save);
    }
}