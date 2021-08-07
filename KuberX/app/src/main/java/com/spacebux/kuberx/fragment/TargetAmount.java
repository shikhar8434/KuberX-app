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

import android.text.Editable;
import android.text.TextWatcher;
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

public class TargetAmount extends Fragment {

    private Context context;
    private TextInputEditText amount;
    private Button save;
    private NavController navController;
    private Wallet wallet;

    public TargetAmount() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_target_amount, container, false);
        setReferences(view);
        configure();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        wallet = getArguments().getParcelable(App.WALLET);
    }

    private void configure() {
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null) {
                    save.setVisibility(View.GONE);
                } else if (s.toString().equals("")) {
                    save.setVisibility(View.GONE);
                } else {
                    save.setVisibility(View.VISIBLE);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(amount.getText()).toString().equals("")) {
                    App.showToast(context, "Amount is required");
                    amount.requestFocus();
                    return;
                }
                wallet.setTarget(Double.valueOf(amount.getText().toString()));
                Bundle bundle = new Bundle();
                bundle.putParcelable(App.WALLET, wallet);
                navController.navigate(R.id.action_targetAmount_to_deadline, bundle);
            }
        });
    }

    private void setReferences(View view) {
        context = getContext();
        amount = view.findViewById(R.id.amount);
        save = view.findViewById(R.id.save);
    }
}