package com.example.blooddonorapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    AlertDialog.Builder alertDialog;

    Fragment fragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar_MainActivity);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileDetailsActivity()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(getApplicationContext(), "Mkae a Request", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsActivity()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(getApplicationContext(), "Mkae a Request", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_logout:
                Toast.makeText(this, "Yug Mewada", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_makeReq:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MakeNewRequest()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(getApplicationContext(), "Mkae a Request", Toast.LENGTH_LONG).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            alertDialog = new AlertDialog.Builder(this);
            alertDialog.setIcon(R.mipmap.ic_icon);
            alertDialog.setMessage("Are you sure want to exit..?");
            alertDialog.setTitle("Blood Donation App");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();
        }
    }
}

