package com.spacebux.kuberx;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;
import com.spacebux.kuberx.adapter.WalletsAdapter;
import com.spacebux.kuberx.model.Wallet;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private Button add;
    private RecyclerView recyclerView;
    private WalletsAdapter adapter;
    private final List<Wallet> wallets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setReferences();
        configure();
        fetchCurrentWallets();
    }

    private void setReferences() {
        context = this;
        add = findViewById(R.id.add);
        adapter = new WalletsAdapter(context, wallets);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    private void configure() {
        ActivityResultLauncher<Void> launcher = registerForActivityResult(new Contract(), new ActivityResultCallback<Wallet>() {
            @Override
            public void onActivityResult(Wallet result) {
                if (result == null) {
                    Log.i(App.TAG, "null wallet");
                    return;
                }
                createWallet(result);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch(null);
            }
        });
    }

    private class Contract extends ActivityResultContract<Void, Wallet> {

        @NonNull
        @NotNull
        @Override
        public Intent createIntent(@NonNull @NotNull Context context, Void input) {
            return new Intent(context, AddWalletActivity.class);
        }

        @Override
        public Wallet parseResult(int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent intent) {
            if (resultCode != RESULT_OK || intent == null) {
                return null;
            }
            return (Wallet) intent.getSerializableExtra(App.WALLET);
        }
    }

    private void createWallet(Wallet wallet) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getUid() == null) {
            return;
        }
        db.collection(App.USERS).document(FirebaseAuth.getInstance().getUid()).collection(App.WALLETS).add(wallet).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Log.i(App.TAG, "Successful");
                } else {
                    Log.i(App.TAG, "Unsuccessful");
                }
            }
        });
    }

    private void fetchCurrentWallets() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getUid() == null)
            return;
        db.collection(App.USERS).document(FirebaseAuth.getInstance().getUid()).collection(App.WALLETS).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.i(App.TAG, error.getMessage());
                    return;
                }
                if (value == null) {
                    Log.i(App.TAG, "Value is null");
                    return;
                }
                wallets.clear();
                adapter.notifyDataSetChanged();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Wallet wallet = snapshot.toObject(Wallet.class);
                    if (wallet == null)
                        return;
                    wallet.setId(snapshot.getId());
                    wallets.add(wallet);
                    adapter.notifyItemInserted(wallets.size() - 1);
                }
            }
        });
    }
}