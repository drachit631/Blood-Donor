package com.example.blooddonorapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageUploadActivity extends AppCompatActivity {
    StorageReference storageReference;
    CircleImageView imageView;
    private static Uri uri;
    Button button;
    int REQ_CODE = 7;
    AlertDialog.Builder alertDialog;
    ProgressDialog pg;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        Toolbar toolbar = findViewById(R.id.toolbar_UI);
        setSupportActionBar(toolbar);
        storageReference = FirebaseStorage.getInstance().getReference("User's Profile Images");


        imageView = findViewById(R.id.image_upload);
        button = findViewById(R.id.btnUploadImage);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chekAndRequestForPermisson();
            }
        });
    }

 //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //FOR UPLOADING IMAGE INTO DATABASE
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            try {
                uri = data.getData();
                imageView.setImageURI(uri);
                pg = new ProgressDialog(ImageUploadActivity.this);
                pg.setMessage("Please Wait");
                pg.show();

                final StorageReference mRef = storageReference.child(System.currentTimeMillis() + "." + getExtention(uri));
                mRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                Uri downloadUri = uri;
                                String myURL = downloadUri.toString();
                                hashMap.put("ImageUrl", myURL);
                                FirebaseDatabase.getInstance().getReference("Donner Profile Details")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("ImageUrl")
                                        .updateChildren(hashMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        alertDialog = new AlertDialog.Builder(ImageUploadActivity.this);
                                                        alertDialog.setTitle("Register Successful");
                                                        alertDialog.setIcon(R.mipmap.ic_icon);
                                                        alertDialog.setMessage("Welcome To Blood Donation App");
                                                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                startActivity(new Intent(ImageUploadActivity.this, LoginActivity.class));
                                                                finish();
                                                            }
                                                        });
                                                        alertDialog.show();
                                                    }
                                                }, 1500);
                                            }
                                        });
                            }
                        });
                    }
                });


            } catch (Exception ee) {Toast.makeText(this, "Please Select An Image", Toast.LENGTH_SHORT).show();}
        }
    }

    private String getExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void chekAndRequestForPermisson() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Please Accept For Required Permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_CODE);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Image"), REQ_CODE);
        }
    }

}
