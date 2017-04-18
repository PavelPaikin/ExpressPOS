package com.dev.lakik.expresspos.SplashFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lakik.expresspos.Helpers.InputValidationHelper;
import com.dev.lakik.expresspos.Helpers.myCrypt;
import com.dev.lakik.expresspos.Model.Company;
import com.dev.lakik.expresspos.Model.ProductImage;
import com.dev.lakik.expresspos.R;
import com.github.clans.fab.FloatingActionButton;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends Fragment {

    private TextInputEditText emailET, pass1ET, pass2ET, companynameET;
    private TextInputLayout emailETWrap, pass1ETWrap, pass2ETWrap, companynameETWrap;
    private CircleImageView imgCompanyPic;
    private Button submitButton;

    private FloatingActionButton fabTakeImage;

    private RegistrationFormValidator validator;

    private myCrypt crypt;

    private static final int CAMERA_INTENT = 1;
    private String imageLocation;

    public RegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        validator = new RegistrationFormValidator();
        crypt = new myCrypt();

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        emailET = (TextInputEditText) view.findViewById(R.id.reg_emailET);
        pass1ET = (TextInputEditText) view.findViewById(R.id.reg_passwordET);
        pass2ET = (TextInputEditText) view.findViewById(R.id.reg_password2ET);
        companynameET = (TextInputEditText) view.findViewById(R.id.reg_companyNameET);
        submitButton = (Button) view.findViewById(R.id.reg_submitButton);

        emailETWrap = (TextInputLayout) view.findViewById(R.id.reg_emailETWrap);
        pass1ETWrap = (TextInputLayout) view.findViewById(R.id.reg_passwordETWrap);
        pass2ETWrap = (TextInputLayout) view.findViewById(R.id.reg_password2ETWrap);
        companynameETWrap = (TextInputLayout) view.findViewById(R.id.reg_companyNameETWrap);

        imgCompanyPic = (CircleImageView)view.findViewById(R.id.registerCompanyImage);

        fabTakeImage = (FloatingActionButton)view.findViewById(R.id.fab_take_picture);
        fabTakeImage.setOnClickListener(btnTakePictureListener);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, password1, password2, companyName;
                email = emailET.getText().toString();
                password1 = pass1ET.getText().toString();
                password2 = pass2ET.getText().toString();
                companyName = companynameET.getText().toString();

                if(!validator.isCompanyNameFieldValid(companyName)){
                    return;
                }

                if(!validator.isEmailFieldValid(email)){
                    return;
                }

                if(!validator.isPassword1FieldValid(password1)){
                    return;
                }

                if(!validator.isPassword2FieldValid(password2)){
                    return;
                }

                if(!validator.isPasswordsEqual(password1, password2)){
                    return;
                }

                String encryptedPass = crypt.encrypt(password1);
                if(!validator.isNullOrEmpty(encryptedPass)) {
                    Company newCompany = new Company(companyName, email, encryptedPass, email, imageLocation);
                    newCompany.save();
                    newCompany.saveOnDevice(getActivity());
                    getActivity().onBackPressed();
                }else {
                    Toast.makeText(getContext(), "Error! Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    View.OnClickListener btnTakePictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            File picture = null;
            try{
                picture = createImage();
            }catch(IOException e){
                e.printStackTrace();
            }
            Intent i = new Intent();
            i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
            if(i.resolveActivity(getActivity().getPackageManager()) != null){
                startActivityForResult(i, CAMERA_INTENT);
            }
        }
    };

    private File createImage() throws IOException {
        //Create a timestamp to help create a collision free name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHss").format(new Date());
        //Create the name of the image
        String fileName = "expresspos" + timeStamp;
        //Grab the directory we want to save the image
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //Create the image in that directory
        File picture = File.createTempFile(fileName, ".jpg", directory);
        //Save the location of the image
        imageLocation = picture.getAbsolutePath();
        return picture;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_INTENT && resultCode == RESULT_OK){

            File pic = new File(imageLocation);
            Crop.of(Uri.fromFile(pic), Uri.fromFile(pic)).asSquare().start(getActivity());
        }

        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            Picasso.with(getContext()).load(new File(imageLocation)).resize(300, 300).centerInside().into(imgCompanyPic);
        }
    }

    class RegistrationFormValidator extends InputValidationHelper{

        public boolean isCompanyNameFieldValid(String text){

            if(isNullOrEmpty(text)){
                companynameETWrap.setError("Company Name can't be empty");
                return false;
            }

            companynameETWrap.setErrorEnabled(false);
            return true;
        }

        public boolean isEmailFieldValid(String text){

            if(isNullOrEmpty(text)){
                emailETWrap.setError("Email can't be empty");
                return false;
            }

            if(!isValidEmail(text)){
                emailETWrap.setError("Incorrect Email format");
                return false;
            }

            emailETWrap.setErrorEnabled(false);
            return true;
        }

        public boolean isPassword1FieldValid(String text){

            if(isNullOrEmpty(text)){
                pass1ETWrap.setError("Password can't be empty");
                return false;
            }

            pass1ETWrap.setErrorEnabled(false);
            return true;
        }

        public boolean isPassword2FieldValid(String text){

            if(isNullOrEmpty(text)){
                pass2ETWrap.setError("Password can't be empty");
                return false;
            }

            pass2ETWrap.setErrorEnabled(false);
            return true;
        }

        public boolean isPasswordsEqual(String pass1, String pass2){

            if(!pass1.equals(pass2)){
                pass2ETWrap.setError("Please repeat password");
                return false;
            }

            pass2ETWrap.setErrorEnabled(false);
            return true;
        }




    }

}
