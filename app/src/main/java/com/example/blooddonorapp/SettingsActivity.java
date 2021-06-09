package com.example.blooddonorapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends Fragment {
    FirebaseAuth firebaseAuth;
    CardView updateCardView, updateImageCardView, logoutCardView;

    @Override
    public View  onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_settings,container,false);
        Toolbar toolbar = view.findViewById(R.id.toolbar_SETTINGS);
        firebaseAuth=FirebaseAuth.getInstance();
        updateCardView = view.findViewById(R.id.cardViewProfileUpdate);

        return view;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //METHOD FOR DELETE ACCOUNT
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        updateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
