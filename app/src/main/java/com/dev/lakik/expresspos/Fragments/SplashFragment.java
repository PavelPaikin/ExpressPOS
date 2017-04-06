package com.dev.lakik.expresspos.Fragments;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
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
import android.widget.TextView;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.CompanyHelper;
import com.dev.lakik.expresspos.Model.Company;
import com.dev.lakik.expresspos.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SplashFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SplashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SplashFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public SplashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SplashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SplashFragment newInstance(String param1, String param2) {
        SplashFragment fragment = new SplashFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ImageView logo;
    EditText loginET, passwordET;
    CheckBox rememberCheck;
    Button loginButton, registerButton;

    ArrayList<View> slideInArray; //an array of objects that should slide into view

    boolean validLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        new DBHelper(this.getActivity());

        //Get display size to position items off screen
        Display display = this.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        //Initialize the array list
        slideInArray = new ArrayList<>();

        //initialize objects
        logo = (ImageView) view.findViewById(R.id.splash_logo);
        loginET = (EditText) view.findViewById(R.id.loginEdittext);
        passwordET = (EditText) view.findViewById(R.id.passwordEdittext);
        rememberCheck = (CheckBox) view.findViewById(R.id.remembermeCheck);
        loginButton = (Button) view.findViewById(R.id.loginButton);
        registerButton = (Button) view.findViewById(R.id.registerButton);

        //Add the edit texts and buttons to the array
        slideInArray.add(loginET);
        slideInArray.add(passwordET);
        slideInArray.add(rememberCheck);
        slideInArray.add(loginButton);

        //Position objects offscreen
        loginET.setX(width + 10);
        passwordET.setX(width + 10);
        rememberCheck.setX(width + 10);
        loginButton.setX(width + 10);
        registerButton.setX(width + 110);

        //Fade the logo into view
        Animation logoAnim = AnimationUtils.loadAnimation(this.getContext(), R.anim.fade_in);
        logo.startAnimation(logoAnim);

        //Move the logo into its up position
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.move_up);
                set.setTarget(logo);
                set.start();
            }
        }.start();

        //Slide in the login fields and buttons
        new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                for (int i = 0; i < slideInArray.size(); i++) { //animate each object in the array to slide it in. This introduces a slight delay between each animation of each object, but it is almost imperceptible and adds flair
                    AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.move_left);
                    set.setTarget(slideInArray.get(i));
                    set.start();
                }

                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.move_left_less);
                set.setTarget(registerButton);
                set.start();
            }
        }.start();



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String login = loginET.getText().toString();
                String password = passwordET.getText().toString();

                ArrayList<Company> companyArray;
                companyArray = CompanyHelper.getAllRecords();
                System.out.println(companyArray);

                for (Company item : companyArray) {
                    if ((item.getLogin().equalsIgnoreCase(login)) && (item.getPassword().equals(password))) {
                        validLogin = true;
                        break;
                    } else {
                        validLogin = false;
                    }
                }

                if (validLogin) {
                    // TODO: 4/5/2017 Redirect user to the homepage screen
                    System.out.println("VALID LOGIN");
                } else {
                    passwordET.setText("");
                    passwordET.setHint("Incorrect Login Details");
                }

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_content, new RegisterFragment()).commit();
            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
