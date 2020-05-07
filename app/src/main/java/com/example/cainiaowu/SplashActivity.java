package com.example.cainiaowu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cainiaowu.LoginActivity.LoginPage;
import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;


public class SplashActivity extends AppCompatActivity {

    private GifImageView gifImageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        gifImageView = findViewById(R.id.gifImageView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(progressBar.VISIBLE);

        //set GifImageView resource
        try {
            InputStream inputStream = getAssets().open("cainiaologo.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        }
        catch(IOException ex){}

        //Start login activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.this.startActivity(new Intent(getApplicationContext(), LoginPage.class));
                SplashActivity.this.finish();
            }
        },3800);

    }
}
