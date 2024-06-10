package com.example.project_rajut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    VideoView videoView;
    Button button;

    TextInputEditText password, username;
    ProgressDialog progressDialog;

    DatabaseReference reference;

    FirebaseAuth mAuth;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "username";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btnlogin);
        username = findViewById(R.id.usernamelogin);
        password = findViewById(R.id.passwordlogin);
        progressDialog = new ProgressDialog(MainActivity.this);
        Bitmap largeicon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        // String name = sharedPreferences.getString(KEY_NAME, null);

        mAuth = FirebaseAuth.getInstance();

        // Cek apakah pengguna saat ini masuk
        if (mAuth.getCurrentUser() != null) {
            Intent login = new Intent(MainActivity.this, Dashboard.class);
            startActivity(login);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("notification", "notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        /* if (name != null){
            Intent login = new Intent(MainActivity.this, Dashboard.class);
            startActivity(login);
        } */

        /*
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.template_login);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                 mediaPlayer.setLooping(true);
            }
        }); */


        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                if (!validateUsername() | !validatePassword()){

                } else {
                    if (checkNetworkConnection()) {

                        String checkUsername = username.getText().toString().trim();
                        String checkPassword = password.getText().toString().trim();

                        reference = FirebaseDatabase.getInstance().getReference("pengguna");
                        Query checkUserDatabase = reference.orderByChild("username").equalTo(checkUsername);

                        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    username.setError(null, null);
                                    String passwordFromDB = snapshot.child(checkUsername).child("password").getValue(String.class);
                                    String Email = snapshot.child(checkUsername).child("email").getValue(String.class);

                                    // mAuth.createUserWithEmailAndPassword(Email, password.getText().toString());

                                    if (!Objects.equals(passwordFromDB, checkPassword)) {
                                        password.setError("Password anda salah", null);
                                        password.requestFocus();
                                    } else {
                                        mAuth.signInWithEmailAndPassword(Email, password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()){
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString(KEY_NAME, username.getText().toString());
                                                    editor.putString(KEY_PASSWORD, password.getText().toString());
                                                    editor.apply();

                                                    progressDialog.setTitle("Loading");
                                                    progressDialog.setMessage("Dimohon untuk tunggu");
                                                    progressDialog.show();

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getApplicationContext(), "Anda berhasil login", Toast.LENGTH_SHORT).show();
                                                            progressDialog.cancel();
                                                        }
                                                    }, 2000);

                                                    password.setText("");

                                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "notification");
                                                    builder.setContentTitle("Informasi");
                                                    builder.setContentText("Anda berhasil login");
                                                    builder.setSmallIcon(R.drawable.logo);
                                                    builder.setLargeIcon(largeicon);
                                                    builder.setAutoCancel(true);

                                                    //MediaPlayer lagunotifikasi = MediaPlayer.create(MainActivity.this, R.raw.lagunotifikasi);
                                                    //lagunotifikasi.start();

                                                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                                                    managerCompat.notify(1, builder.build());

                                                    Intent register;
                                                    register = new Intent(MainActivity.this, Dashboard.class);
                                                    startActivity(register);
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Autentikasi firebase anda tidak berhasil", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    username.setError("user tidak di temukan");
                                    username.requestFocus();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });

                    } else {
                        progressDialog.setTitle("Loading");
                        progressDialog.setMessage("Dimohon untuk tunggu");
                        progressDialog.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Internet anda tidak ada", Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        }, 2000);
                    }
                }
            }
        });
    }


    /* @Override
    protected void onRestart() {
        videoView.start();
        super.onRestart();
    } */

    public Boolean validateUsername(){
        String val = username.getText().toString();
        if (val.isEmpty()){
            username.setError("Username harus ditulis");
            return false;
        }
        else {
            username.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = password.getText().toString();
        if (val.isEmpty()){
            password.setError("Password harus ditulis");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)  this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}