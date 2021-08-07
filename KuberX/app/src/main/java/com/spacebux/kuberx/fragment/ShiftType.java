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

public class ShiftType extends Fragment {

    private Context context;
    private Button weekly, monthly, save;
    private NavController navController;
    private int shift = 0;
    private Wallet wallet;

    public ShiftType() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shift_type, container, false);
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
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shift = App.WEEKLY;
                save.setVisibility(View.VISIBLE);
                weekly.setBackgroundColor(getResources().getColor(R.color.color3, context.getTheme()));
                monthly.setBackgroundColor(getResources().getColor(R.color.color4, context.getTheme()));
            }
        });
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shift = App.MONTHLY;
                save.setVisibility(View.VISIBLE);
                monthly.setBackgroundColor(getResources().getColor(R.color.color3, context.getTheme()));
                weekly.setBackgroundColor(getResources().getColor(R.color.color4, context.getTheme()));
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallet.setShiftType(shift);
                Bundle bundle = new Bundle();
                bundle.putParcelable(App.WALLET, wallet);
                navController.navigate(R.id.action_shiftType_to_targetAmount, bundle);
            }
        });
    }

    private void setReferences(View view) {
        context = getContext();
        weekly = view.findViewById(R.id.weekly);
        monthly = view.findViewById(R.id.monthly);
        save = view.findViewById(R.id.save);
    }
}