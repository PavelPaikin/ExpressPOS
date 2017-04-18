package com.dev.lakik.expresspos.SplashFragments;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.CompanyHelper;
import com.dev.lakik.expresspos.MainActivity;
import com.dev.lakik.expresspos.Model.Company;
import com.dev.lakik.expresspos.R;

import java.util.ArrayList;

public class SplashFragment extends Fragment {

    public SplashFragment() {
        // Required empty public constructor
    }

    public static SplashFragment newInstance() {
        SplashFragment fragment = new SplashFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ImageView logo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        logo = (ImageView)view.findViewById(R.id.splash_logo);

        //Fade the logo into view
        Animation logoAnim = AnimationUtils.loadAnimation(this.getContext(), R.anim.fade_in);
        logo.startAnimation(logoAnim);
        logoAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.splash_mainContainer, new LoginFragment()).commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        return view;
    }

}
