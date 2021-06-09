package com.example.blooddonorapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    TextInputLayout inputLayout;
    Toolbar toolbar;
    Button button;
    FirebaseAuth firebaseAuth;
    Utilsclass utilsclass;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        toolbar=findViewById(R.id.toolbar_RYP);
        inputLayout=findViewById(R.id.input_resetPassword);
        inputLayout.getEditText().requestFocus();
        button=findViewById(R.id.btnSendPasswordResetEmail);
        builder=new AlertDialog.Builder(this);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        firebaseAuth=FirebaseAuth.getInstance();
        utilsclass=new Utilsclass();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utilsclass.checkConnection(getApplicationContext())==null){
                    Toast.makeText(ForgetPasswordActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                } else if (inputLayout.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(ForgetPasswordActivity.this, "Please Enter Valid Email", Toast.LENGTH_LONG).show();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(inputLayout.getEditText().getText().toString()).matches()){
                    inputLayout.setError("Invalid Email address");
                }else {
                    inputLayout.setError(null);
                    sendEmail();
                    inputLayout.getEditText().setText(null);
                }
            }
        });
    }

    private void sendEmail() {
        firebaseAuth.sendPasswordResetEmail(inputLayout.getEditText().getText().toString())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgetPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        inputLayout.getEditText().setText(null);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            builder.setIcon(R.mipmap.ic_icon);
                            builder.setTitle("Blood Donation App");
                            builder.setMessage("Email Sent Successfully");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(ForgetPasswordActivity.this,WelcomeActivity.class));
                                    finish();
                                }
                            });
                        }
                    }
                });
    }
}
