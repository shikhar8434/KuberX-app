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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.spacebux.kuberx.App;
import com.spacebux.kuberx.R;
import com.spacebux.kuberx.adapter.WalletsAdapter;
import com.spacebux.kuberx.model.Wallet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WalletHome extends Fragment {
    public WalletHome() {
        // Required empty public constructor
    }

    private Context context;
    private Button add;
    private RecyclerView recyclerView;
    private WalletsAdapter adapter;
    private NavController navController;
    private final List<Wallet> wallets = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallet_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setReferences(view);
        configure();
        fetchCurrentWallets();
        if (getArguments() != null) {
            createWallet(getArguments().getParcelable(App.WALLET));
        }
    }

    private void setReferences(View view) {
        context = getContext();
        add = view.findViewById(R.id.add);
        navController = Navigation.findNavController(view);
        adapter = new WalletsAdapter(context, navController, wallets);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    private void configure() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_walletHome_to_goalName);
            }
        });
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