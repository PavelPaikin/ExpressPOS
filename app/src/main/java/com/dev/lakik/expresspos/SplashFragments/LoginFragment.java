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
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.LinearLayout;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.CompanyHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.InventoryHelper;
import com.dev.lakik.expresspos.Helpers.InputValidationHelper;
import com.dev.lakik.expresspos.Helpers.myCrypt;
import com.dev.lakik.expresspos.MainActivity;
import com.dev.lakik.expresspos.Model.Company;
import com.dev.lakik.expresspos.R;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ImageView logo;
    private EditText loginET, passwordET;
    private CheckBox rememberCheck;
    private Button loginButton, registerButton;

    private ArrayList<View> slideInArray; //an array of objects that should slide into view

    private boolean validLogin;

    private LinearLayout lvLogoFormContainer;

    private TextInputLayout loginWrap;
    private TextInputLayout passWrap;

    InputValidationHelper validator;

    private myCrypt crypt;

    Company company;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        crypt = new myCrypt();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        validator = new InputValidationHelper();
        new DBHelper(this.getActivity());

        //initialize objects
        logo = (ImageView) view.findViewById(R.id.splash_logo);
        loginET = (EditText) view.findViewById(R.id.loginEdittext);
        passwordET = (EditText) view.findViewById(R.id.passwordEdittext);
        rememberCheck = (CheckBox) view.findViewById(R.id.remembermeCheck);
        loginButton = (Button) view.findViewById(R.id.loginButton);
        registerButton = (Button) view.findViewById(R.id.registerButton);

        lvLogoFormContainer = (LinearLayout)view.findViewById(R.id.lvLogoFormContainer);
        lvLogoFormContainer.setVisibility(View.INVISIBLE);

        loginWrap = (TextInputLayout)view.findViewById(R.id.loginEdittextWrap);
        passWrap = (TextInputLayout)view.findViewById(R.id.passwordEdittextWrap);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String login = loginET.getText().toString();
                String password = passwordET.getText().toString();

                if(validator.isNullOrEmpty(login)){
                    loginWrap.setError("Email can't be empty");
                    return;
                }else{
                    loginWrap.setErrorEnabled(false);
                }

                if(validator.isNullOrEmpty(password)){
                    passWrap.setError("Password can't be empty");
                    return;
                }else{
                    passWrap.setErrorEnabled(false);
                }

                if(!validator.isValidEmail(login)){
                    loginWrap.setError("Incorrect email format");
                    return;
                }else{
                    loginWrap.setErrorEnabled(false);
                }

                String encryptedPassword = crypt.encrypt(password);
                if(!encryptedPassword.equals("")) {

                    company = Company.get(login);

                    if (company != null) {
                        loginWrap.setErrorEnabled(false);

                        if (company.getPassword().equals(encryptedPassword)) {
                            validLogin = true;
                            passWrap.setErrorEnabled(false);
                        } else {
                            passWrap.setError("Incorect password");
                        }
                    } else {
                        loginWrap.setError("Email doesn't exist");
                    }

                    if (validLogin) {
                        // TODO: 4/5/2017 Redirect user to the homepage screen
                        System.out.println("VALID LOGIN");
                        if(rememberCheck.isChecked()) {
                            company.setAutoLogin();
                        }else{
                            company.removeAutoLogin();
                        }
                        company.saveOnDevice(getActivity());

                        openMainApp();
                    }
                }
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();

                transaction.addToBackStack(null);
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.splash_mainContainer, new RegisterFragment());
                transaction.commit();

            }
        });



        return view;
    }

    private void openMainApp(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void startAnimation(){
        Animation logoAnim = AnimationUtils.loadAnimation(getContext(), R.anim.move_to_top);
        logoAnim.setDuration(1000);
        logoAnim.setFillAfter(true);
        logo.startAnimation(logoAnim);

        logoAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                lvLogoFormContainer.setVisibility(View.VISIBLE);
                Animation logoAnim = AnimationUtils.loadAnimation(getContext(), R.anim.move_from_right);
                logoAnim.setDuration(1000);
                logoAnim.setFillAfter(true);
                lvLogoFormContainer.startAnimation(logoAnim);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        company = new Company();
        company.loadFromDevice(getActivity());

        if(company.isAutoLogInRequered()){
            openMainApp();
        }else{
            loginET.setText(company.getEmail());
            startAnimation();
        }
    }
}
