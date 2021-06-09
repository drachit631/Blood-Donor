package com.example.blooddonorapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

public class LoginActivity extends AppCompatActivity implements InternetConnectivityListener {

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    Utilsclass utilsclass;
    TextView textView;
    TextInputLayout layout1, layout2;
    Button button,forgetPassButton;
    Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = findViewById(R.id.toolbar_LOGIN);
        setSupportActionBar(toolbar);
        utilsclass=new Utilsclass();

        InternetAvailabilityChecker mInternetAvailabilityChecker = InternetAvailabilityChecker.init(LoginActivity.this);
        mInternetAvailabilityChecker.addInternetConnectivityListener(LoginActivity.this);
        builder = new AlertDialog.Builder(this);

        progressDialog = new ProgressDialog(this);
        layout1 = findViewById(R.id.email_for_login);
        layout2 = findViewById(R.id.pass_for_login);


        textView = findViewById(R.id.tx_signup);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utilsclass.checkConnection(getApplicationContext())==null){
                    Toast.makeText(LoginActivity.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }else {
                    startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                    finish();
                }
            }
        });


        button = findViewById(R.id.mybutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utilsclass.checkConnection(getApplicationContext()) == null) {
                    Toast.makeText(LoginActivity.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();
                } else if (layout1.getEditText().getText().toString().isEmpty() || layout2.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Couldn't Sign In With Empty Fields", Toast.LENGTH_LONG).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(layout1.getEditText().getText().toString()).matches()) {
                    layout1.setError("Invalid Email");
                } else {
                    layout1.setError(null);
                    login();
                }
            }
        });

        forgetPassButton=findViewById(R.id.btnForgetPass);
        forgetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
                finish();
            }
        });

    }

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //LOGIN WITH EMAIL AND PASSWORD
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void login() {
        firebaseAuth.signInWithEmailAndPassword(layout1.getEditText().getText().toString(), layout2.getEditText().getText().toString())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        layout1.getEditText().setText(null);
                        layout2.getEditText().setText(null);
                        builder.setIcon(R.mipmap.ic_icon);
                        builder.setTitle("Invalid Credentials");
                        builder.setCancelable(false);
                        builder.setMessage("Bad User Name Or Password");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                layout1.getEditText().requestFocus();
                            }
                        });
                        builder.show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.setMessage("Please Wait");
                            progressDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }, 2000);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        builder.setCancelable(false);
        builder.setTitle("Blood Donation App");
        builder.setIcon(R.mipmap.ic_icon);
        builder.setMessage("Are You Sure Want To Exit");
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (!isConnected) {
            toolbar.setTitle("No Internet Connection");
        } else {
           toolbar.setTitle("Login With Existing Account");
        }
    }
}