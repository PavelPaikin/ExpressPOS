package com.dev.lakik.expresspos;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.lakik.expresspos.Fragments.RegisterFragment;
import com.dev.lakik.expresspos.Fragments.SplashFragment;

public class SplashActivity extends AppCompatActivity
        implements SplashFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportFragmentManager().beginTransaction().replace(R.id.splash_mainContainer, new SplashFragment()).commit();


    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
