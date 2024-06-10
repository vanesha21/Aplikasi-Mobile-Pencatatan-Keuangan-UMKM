package com.example.project_rajut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    SharedPreferences sharedPreferences;

    DatabaseReference databaseReference;

    FirebaseAuth mAuth;

    StorageReference storageRef, imageRef;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "username";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawerlayouts);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String Username = sharedPreferences.getString("username", "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Temukan header dari NavigationView
        View headerView = navigationView.getHeaderView(0);

        // Temukan textview dengan id "tv_nama"
        TextView Nama = headerView.findViewById(R.id.textheader);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();

        if (uid != null){
            // Create a storage reference
            FirebaseStorage storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference("images/"+uid+".jpg");

            // Set the image to the ImageView
            CircleImageView imageView = headerView.findViewById(R.id.gambarheader);

            // Download the image from storage
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Image successfully downloaded
                    System.out.println("berhasil mengambil gambar");

                    Glide.with(Dashboard.this)
                            .load(uri)
                            .override(400, 400)
                            .error(R.drawable.profile)
                            .into(imageView);

                    //imageView.setImageURI(uri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imageRef = storage.getReference("images/"+uid+".png");

                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(Dashboard.this)
                                    .load(uri)
                                    .error(R.drawable.profile)
                                    .into(imageView);
                        }
                    });
                }
            });

        }

        databaseReference = FirebaseDatabase.getInstance().getReference("pengguna");
        Query query = databaseReference.orderByChild("username").equalTo(Username);

        // Tambahkan listener
        query.addValueEventListener(new ValueEventListener() {
            String nama = null;
            @Override
            public void onDataChange(DataSnapshot Snapshot) {

                for (DataSnapshot dataSnapshotChild : Snapshot.getChildren()) {
                    // Ambil harga
                    String price = dataSnapshotChild.child("username").getValue(String.class);

                    // Tambahkan harga ke sum
                    nama = price;
                }

                String teksbesar = nama.substring(0, 1).toUpperCase() + nama.substring(1);
                Nama.setText(teksbesar);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new berandaFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
            builder.setIcon(R.drawable.exit);
            builder.setTitle("Keluar");
            builder.setMessage("apakah kamu ingin keluar");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent berandapertama = new Intent(Intent.ACTION_MAIN);
                    berandapertama.addCategory(Intent.CATEGORY_HOME);
                    berandapertama.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(berandapertama);
                }
            });
            builder.setNegativeButton("Cancel", null);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.home){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new berandaFragment()).commit();
            return true;
        }
        if (id == R.id.keluar){
            final AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
            builder.setIcon(R.drawable.exit);
            builder.setTitle("Keluar");
            builder.setMessage("apakah kamu ingin keluar");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    // Sign the user out
                    mAuth.signOut();

                    finish();


                    /* SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    finish(); */
                }
            });
            builder.setNegativeButton("Cancel", null);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();


        }

        return false;
    }
}