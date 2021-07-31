package com.spacebux.kuberx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.spacebux.kuberx.model.Wallet;

import java.util.Objects;

public class AddWalletActivity extends AppCompatActivity {

    private Context context;
    private TextInputEditText title, amount, weeks;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        setReferences();
        configure();
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
                bundle.putSerializable(App.WALLET, wallet);
                setResult(RESULT_OK, new Intent().putExtras(bundle));
                finish();
            }
        });
    }

    private void setReferences() {
        context = this;
        title = findViewById(R.id.title);
        amount = findViewById(R.id.amount);
        weeks = findViewById(R.id.weeks);
        save = findViewById(R.id.save);
    }
}