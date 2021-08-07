package com.spacebux.kuberx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {
    private NavController navController;
    private FloatingActionButton addFab;
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        navController.navigate(R.id.frag1);
        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setBackground(null);
        contextOfApplication = getApplicationContext();
        addFab = findViewById(R.id.fab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.frag2);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.posts:
                    item.setIcon(R.drawable.ic_launcher_background);
                    navController.navigate(R.id.frag1);
                    return true;
                case R.id.wallet:
                    item.setIcon(R.drawable.ic_baseline_account_balance_wallet_24);
                    navController.navigate(R.id.walletHome);
                    return true;
                case R.id.fab:
                    item.setIcon(R.drawable.ic_launcher_background);
                    navController.navigate(R.id.frag2);
                    return true;
                case R.id.memories2:
                    item.setIcon(R.drawable.ic_launcher_background);
                    navController.navigate(R.id.frag4);
                    return true;
                case R.id.profile2:
                    item.setIcon(R.drawable.ic_launcher_background);
                    navController.navigate(R.id.frag5);
                    return true;

            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
}

