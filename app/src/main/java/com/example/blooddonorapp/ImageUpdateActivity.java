package com.example.blooddonorapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageUpdateActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    StorageReference storageReference;
    CircleImageView imageView;
    Button button;
    private static Uri uri;
    ProgressDialog pg;
    AlertDialog.Builder alertDialog;
    private static int REQ_CODE = 7;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_image);

        Toolbar toolbar = findViewById(R.id.toolbar_UPI);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ImageUpdateActivity.this, MainActivity.class));
                finish();
            }
        });


        imageView = findViewById(R.id.image_update);

        databaseReference = FirebaseDatabase.getInstance().getReference("Donner Profile Details");
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        storageReference = FirebaseStorage.getInstance().getReference("User's Profile Images");
        button = findViewById(R.id.btnUpdateImage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), REQ_CODE);
            }
        });
    }


    private String getExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            try {
                uri = data.getData();
                imageView.setImageURI(uri);
                pg = new ProgressDialog(ImageUpdateActivity.this);
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
                                                alertDialog = new AlertDialog.Builder(ImageUpdateActivity.this);
                                                alertDialog.setTitle("Successful");
                                                alertDialog.setIcon(R.mipmap.ic_icon);
                                                alertDialog.setMessage("Image Uploaded Successfully");
                                                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        startActivity(new Intent(ImageUpdateActivity.this, MainActivity.class));
                                                        finish();
                                                    }
                                                });
                                                alertDialog.show();
                                            }
                                        });
                            }
                        });
                    }
                });


            } catch (Exception ee) {Toast.makeText(this, "Please Select An Image", Toast.LENGTH_SHORT).show();}
        }
    }
}
