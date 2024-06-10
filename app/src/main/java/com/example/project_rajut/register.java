package com.example.project_rajut;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

public class register extends AppCompatActivity {

    TextView link;

    VideoView videoView;
    Button daftar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        link = findViewById(R.id.linklogin);
        videoView = findViewById(R.id.videos);
        daftar = findViewById(R.id.tomboldaftar);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.template_login);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mendaftar;
                mendaftar = new Intent(register.this, MainActivity.class);
                startActivity(mendaftar);
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login;
                login = new Intent(register.this, MainActivity.class);
                startActivity(login);
            }
        });
    }

    @Override
    protected void onRestart() {
        videoView.start();
        super.onRestart();
    }
}