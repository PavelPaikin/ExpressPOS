package com.dev.lakik.expresspos.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dev.lakik.expresspos.Model.Company;
import com.dev.lakik.expresspos.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    EditText emailET, pass1ET, pass2ET, companynameET;
    Button submitButton;
    TextView errorTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        emailET = (EditText) view.findViewById(R.id.reg_emailET);
        pass1ET = (EditText) view.findViewById(R.id.reg_passwordET);
        pass2ET = (EditText) view.findViewById(R.id.reg_password2ET);
        companynameET = (EditText) view.findViewById(R.id.reg_companyNameET);
        submitButton = (Button) view.findViewById(R.id.reg_submitButton);

        errorTV = (TextView) view.findViewById(R.id.reg_errorTV);

        final ArrayList<EditText> etArray = new ArrayList<>();

        etArray.add(emailET);
        etArray.add(pass1ET);
        etArray.add(pass2ET);
        etArray.add(companynameET);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validRegistration = true;

                errorTV.setText("");

                //checks for any empty fields
                for (EditText et : etArray) {
                    if (et.getText().toString().isEmpty()) {
                        errorTV.setText("All fields must be filled!");
                        validRegistration = false;
                    }
                }

                //returns true if the text in the password edit text fields are not the same
                if (!pass1ET.getText().toString().equals(pass2ET.getText().toString())) {
                    errorTV.setText("Passwords do not match!");
                    validRegistration = false;
                }

                //true if all fields are filled and the passwords both match
                if (validRegistration) {
                    String email, password, companyName, imagePath;
                    email = emailET.getText().toString();
                    password = pass1ET.getText().toString();
                    companyName = companynameET.getText().toString();
                    imagePath = ""; // TODO: 4/6/2017 IMAGE PATH MUST BE SET FROM CAMERA API

                    Company newCompany = new Company(companyName, email, password, email, imagePath);
                    newCompany.save();

                    System.out.println("--NEW COMPANY CREATED--");
                    newCompany.printObject();


                }


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
