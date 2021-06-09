package com.example.blooddonorapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetailsActivity extends Fragment {
    TextView textView_name=null, textView_mobile=null, textView_email=null, textView_city=null, textView_area=null, textView_birthdate=null, textView_bloodgrp=null, textView_gender=null;
    CircleImageView imageView;
    RelativeLayout relativeLayout;
    DatabaseReference databaseReference;

    String url = null;
    Utilsclass utilsclass = new Utilsclass();
    Bitmap bitmap;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_req,container,false);
        LinearLayout ll=view.findViewById(R.id.parenter);
        relativeLayout = view.findViewById(R.id.layout_content);
        View view1 = ll.getRootView();

        imageView = view1.findViewById(R.id.image_profile);

        textView_name = view1.findViewById(R.id.nameTextView);
        textView_mobile = view1.findViewById(R.id.mobileTextView);
        textView_email = view1.findViewById(R.id.emailTextView);
        textView_city = view1.findViewById(R.id.cityTextView);
        textView_area = view1.findViewById(R.id.areaTextView);
        textView_birthdate = view1.findViewById(R.id.bdayTextView);
        textView_bloodgrp = view1.findViewById(R.id.bloodTextView);
        textView_gender = view1.findViewById(R.id.genderTextView);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Donner Profile Details");
        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        showProfileDetails();
    }
    public void showProfileDetails() {

        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Donner Profile Details");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").getValue().toString()!=null
                        && dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Email Address").getValue().toString()!=null
                        && dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Contact Number").getValue().toString()!=null
                        && dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("City and State").getValue().toString()!=null
                        && dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Area And Pin Code").getValue().toString()!=null
                        && dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Birth Date").getValue().toString()!=null
                        && dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Blood Group").getValue().toString()!=null
                        && dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Gender").getValue().toString()!=null
                        && dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ImageUrl").child("ImageUrl").getValue().toString()!=null)
                        {


                            textView_name.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").getValue().toString());
                            textView_email.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Email Address").getValue().toString());
                            textView_mobile.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Contact Number").getValue().toString());
                            textView_city.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("City and State").getValue().toString());
                            textView_area.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Area And Pin Code").getValue().toString());
                            textView_birthdate.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Birth Date").getValue().toString());
                            textView_bloodgrp.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Blood Group").getValue().toString());
                            textView_gender.setText(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Gender").getValue().toString());

                            url = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ImageUrl").child("ImageUrl").getValue().toString();
                            Picasso.get()
                                    .load(url)
                                    .fit()
                                    .into(imageView);


                        }else {
                            textView_name.setText("--");
                            textView_email.setText("--");
                            textView_mobile.setText("--");
                            textView_city.setText("--");
                            textView_area.setText("--");
                            textView_birthdate.setText("--");
                            textView_gender.setText("--");
                            textView_bloodgrp.setText("--");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

        } catch (Exception e1) {
            Toast.makeText(getActivity(), e1.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
