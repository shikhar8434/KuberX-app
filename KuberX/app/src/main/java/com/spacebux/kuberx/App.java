package com.spacebux.kuberx;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class App extends Application {
    public static final String TAG = "KuberX";
    public static final String WALLET = "wallet";
    public static final String USERS = "users";
    public static final String WALLETS = "wallets";
    public static final String CURRENT = "current";
    public static final String WALLET_ID = "wallet_id";
    public static final String AMOUNT = "amount";
    public static final String PAYMENT_SUCCESS = "payment_success";
    public static final int WEEKLY = 7;
    public static final int MONTHLY = 30;
    public static final String WEEKS = "weeks";
    public static final String MONTHS = "months";

    public static String getShiftType(int n) {
        if (n == WEEKLY)
            return WEEKS;
        return MONTHS;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
