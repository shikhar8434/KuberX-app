package com.spacebux.kuberx.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.spacebux.kuberx.App;
import com.spacebux.kuberx.R;
import com.spacebux.kuberx.model.Wallet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WalletsAdapter extends RecyclerView.Adapter<WalletsAdapter.CustomVH> {

    private Context context;
    private List<Wallet> wallets;
    private NavController navController;

    public WalletsAdapter(Context context, NavController navController, List<Wallet> wallets) {
        this.context = context;
        this.navController = navController;
        this.wallets = wallets;
    }

    @NonNull
    @NotNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_view, parent, false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WalletsAdapter.CustomVH holder, int position) {
        holder.bindData(wallets.get(position));
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        private TextView title, target, targetDate, current;
        private Button pay, load;

        public CustomVH(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            target = itemView.findViewById(R.id.target);
            targetDate = itemView.findViewById(R.id.target_date);
            current = itemView.findViewById(R.id.current);
            pay = itemView.findViewById(R.id.pay);
            load = itemView.findViewById(R.id.load);
            load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(App.WALLET, wallets.get(getAdapterPosition()));
                    navController.navigate(R.id.action_walletHome_to_enterWalletAmount, bundle);
                }
            });
        }

        protected void bindData(Wallet w) {
            title.setText(w.getTitle());
            target.setText("Target: " + w.getTarget());
            current.setText("Current Balance: " + (w.getCurrent() != null ? w.getCurrent() : "0"));
            targetDate.setText(w.getShifts() + " " + App.getShiftType(w.getShiftType()));
        }
    }
}
