package com.dev.lakik.expresspos;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.lakik.expresspos.SplashFragments.RegisterFragment;
import com.dev.lakik.expresspos.SplashFragments.SplashFragment;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportFragmentManager().beginTransaction().replace(R.id.splash_mainContainer, new SplashFragment()).commit();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getSupportFragmentManager().findFragmentById(R.id.splash_mainContainer).onActivityResult(requestCode, resultCode, data);
    }
}
