package com.example.project_rajut;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class loadingpage extends AppCompatActivity {

    Handler handler;

    VideoView videoView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadingpage);

        videoView = findViewById(R.id.videos);
        handler = new Handler();

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.template_loading);
        videoView.setVideoURI(uri);
        videoView.start();

        if (checkNetworkConnection()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(loadingpage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3300);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(loadingpage.this);
                    builder.setIcon(R.drawable.cycle);
                    builder.setTitle("Loading gagal");
                    builder.setMessage("Internet tidak ada, keluar ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            finish();

                            // Intent intent = new Intent(loadingpage.this, loadingpage.class);
                            // startActivity(intent);
                        }
                    });
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }, 3300);
        }


    }

    @Override
    public void onBackPressed() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)  this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}