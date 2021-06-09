package com.example.blooddonorapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    public static final Pattern PHONE = Pattern.compile("^[0-9]{10}$");
    public static final Pattern NAME = Pattern.compile("^[a-zA-Z\\s]*$");
    public static final Pattern PASSWORD = Pattern.compile("^" +
            "(?=.*[!@#$%*+_])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{4,}" +                // at least 4 characters
            "$");

    Button button;
    Spinner spinner_bg, spinner_states;
    TextInputLayout input_name, input_email,input_phone, input_city, input_area, input_birthdate, input_createPass, input_confPass;
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialog;
    Toolbar toolbar;
    Utilsclass utilsclass;
    RadioButton button_male, button_female;
    private String gender = null;
    RadioGroup radioGroup;
    String contactNo, code, bloodgroup, state;
    CountryCodePicker countryCodePicker;
    ArrayAdapter<String> arrayAdapter_bg, arrayAdapter_states;
    HashMap<String, Object> map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar = findViewById(R.id.toolbar_AP);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        alertDialog = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);

        input_name = findViewById(R.id.txt_input_name);
        input_name.getEditText().requestFocus();

        countryCodePicker = findViewById(R.id.countryCodePicker);
        input_phone = findViewById(R.id.txt_input_contactno);
        input_email = findViewById(R.id.txt_input_email);
        input_city = findViewById(R.id.txt_input_city);
        input_area = findViewById(R.id.txt_input_area);
        input_birthdate = findViewById(R.id.txt_input_birthdate);
        input_createPass = findViewById(R.id.txt_input_password);
        input_confPass = findViewById(R.id.txt_input_confirmPass);

        spinner_bg = findViewById(R.id.spinner_bloodGroup);
        arrayAdapter_bg = new ArrayAdapter<>(RegisterActivity.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.Blood_Groups));
        spinner_bg.setAdapter(arrayAdapter_bg);
        spinner_bg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodgroup=spinner_bg.getSelectedItem().toString();
                Toast.makeText(RegisterActivity.this, bloodgroup, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_states = findViewById(R.id.spinner_State);
        arrayAdapter_states = new ArrayAdapter<>(RegisterActivity.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.States));
        spinner_states.setAdapter(arrayAdapter_states);
        spinner_states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                state=spinner_states.getSelectedItem().toString();
                Toast.makeText(RegisterActivity.this, state, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        

        
        button_male = findViewById(R.id.male);
        button_female = findViewById(R.id.female);
        radioGroup = findViewById(R.id.radio_grp);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male) {
                    gender = "Male";
                    Toast.makeText(RegisterActivity.this, gender, Toast.LENGTH_SHORT).show();
                }
                if (checkedId == R.id.female) {
                    gender = "Female";
                    Toast.makeText(RegisterActivity.this, gender, Toast.LENGTH_SHORT).show();
                }
            }
        });

        input_confPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (input_confPass.getEditText().getText().toString().equals(input_createPass.getEditText().getText().toString())) {
                    input_confPass.setError(null);
                } else {
                    input_confPass.setError("Password Couldn't matched");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        button = findViewById(R.id.btn_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_name.getEditText().getText().toString().isEmpty()
                        || input_email.getEditText().getText().toString().isEmpty() || input_phone.getEditText().getText().toString().isEmpty()
                        || input_city.getEditText().getText().toString().isEmpty() || input_area.getEditText().getText().toString().isEmpty()
                        || input_birthdate.getEditText().getText().toString().isEmpty() || spinner_bg.getSelectedItem().equals("Select blood group")
                        || gender == null || input_createPass.getEditText().toString().isEmpty() || input_confPass.getEditText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Couldn't Register With Empty Fields", Toast.LENGTH_LONG).show();
                    Toast.makeText(RegisterActivity.this, "Please Enter Valid Blood Group", Toast.LENGTH_LONG).show();
                }
                if (!NAME.matcher(input_name.getEditText().getText().toString().trim()).matches()) {
                    input_name.setError("Invalid Name");
                } else if (!PHONE.matcher(input_phone.getEditText().getText().toString().trim()).matches()) {
                    input_phone.setError("Invalid Phone Number");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(input_email.getEditText().getText().toString().trim()).matches()) {
                    input_email.setError("Invalid Email Address");
                } else if (!PASSWORD.matcher(input_createPass.getEditText().getText().toString().trim()).matches()) {
                    input_createPass.setError("Invalid Password");
                } else {
                    code = countryCodePicker.getSelectedCountryCode();
                    contactNo = "+" + code + "" + input_phone.getEditText().getText().toString();
                    map = new HashMap<>();
                    map.put("Name", input_name.getEditText().getText().toString());
                    map.put("Contact Number", contactNo);
                    map.put("Email Address", input_email.getEditText().getText().toString());
                    map.put("City and State", input_city.getEditText().getText().toString() + ", " + state);
                    map.put("Area And Pin Code", input_area.getEditText().getText().toString());
                    map.put("Blood Group", bloodgroup);
                    map.put("Birth Date", input_birthdate.getEditText().getText().toString());
                    map.put("User Password", input_confPass.getEditText().getText().toString());
                    map.put("Gender", gender);


                    input_name.setError("");
                    input_email.setError("");
                    input_phone.setError("");
                    input_confPass.setError("");
                    input_createPass.setError("");

                    CreateAccount();

                    input_name.getEditText().setText(null);
                    input_phone.getEditText().setText(null);
                    input_email.getEditText().setText(null);
                    input_city.getEditText().setText(null);
                    input_area.getEditText().setText(null);
                    input_birthdate.getEditText().setText(null);
                    input_createPass.getEditText().setText(null);
                    input_confPass.getEditText().setText(null);
                }

            }
        });
    }

    private void CreateAccount() {
        utilsclass = new Utilsclass();
        if (utilsclass.checkConnection(getApplicationContext()) == null) {
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Could not register without internet");
            alertDialog.setIcon(R.mipmap.ic_icon);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                    finish();
                }
            });
            alertDialog.show();
        } else {
            try{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(input_email.getEditText().getText().toString(), input_confPass.getEditText().getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_LONG).show();
                                    insertData();
                                }
                            }
                        });

            }catch (Exception ww){

            }
        }
    }

    private void insertData(){
        try{
            FirebaseDatabase.getInstance().getReference("Donner Profile Details")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(map)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        alertDialog.setTitle("Blood Donation App");
                                        alertDialog.setMessage("User registered successfully");
                                        alertDialog.setCancelable(false);
                                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                startActivity(new Intent(RegisterActivity.this, ImageUploadActivity.class));
                                                finish();
                                            }
                                        });
                                        alertDialog.show();
                                    }
                                }, 500);
                            }
                        }
                    });
        }catch (Exception e1){
        }
    }

    @Override
    public void onBackPressed() {
    }
}