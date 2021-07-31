package com.spacebux.kuberx;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthenticationActivity extends AppCompatActivity {

    private Context context;
    private SignInButton googleSignIn;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        setReferences();
        configure();
    }

    private void setReferences() {
        context = this;
        googleSignIn = findViewById(R.id.google_signin);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(context, gso);
    }

    private void configure() {
        googleSignIn.setSize(SignInButton.SIZE_WIDE);
        ActivityResultLauncher<Void> launcher = registerForActivityResult(new Contract(), new ActivityResultCallback<Task<GoogleSignInAccount>>() {
            @Override
            public void onActivityResult(Task<GoogleSignInAccount> result) {
                if (result == null) {
                    Log.i(App.TAG, "null");
                    return;
                }
                try {
                    GoogleSignInAccount account = result.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    Log.w(App.TAG, "signInResult:failed code=" + e.getStatusCode());

                }
            }
        });
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch(null);
            }
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(context, MainActivity.class));
                            finish();
                        } else {
                            Log.w(App.TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private class Contract extends ActivityResultContract<Void, Task<GoogleSignInAccount>> {

        @NonNull
        @org.jetbrains.annotations.NotNull
        @Override
        public Intent createIntent(@NonNull @org.jetbrains.annotations.NotNull Context context, Void input) {
            return gsc.getSignInIntent();
        }

        @Override
        public Task<GoogleSignInAccount> parseResult(int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent intent) {
            if (resultCode == RESULT_OK)
                return GoogleSignIn.getSignedInAccountFromIntent(intent);
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(context, MainActivity.class));
            finish();
        }
    }
}