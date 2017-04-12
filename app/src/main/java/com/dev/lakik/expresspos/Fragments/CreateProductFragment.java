package com.dev.lakik.expresspos.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dev.lakik.expresspos.Helpers.InputValidationHelper;
import com.dev.lakik.expresspos.Model.Const;
import com.dev.lakik.expresspos.Model.Inventory;
import com.dev.lakik.expresspos.Model.Product;
import com.dev.lakik.expresspos.R;

public class CreateProductFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private TextInputEditText etProductName;
    private TextInputEditText etProductNumber;
    private TextInputEditText etUPC;
    private TextInputEditText etDescription;
    private TextInputEditText etAmount;
    private TextInputEditText etPrice;
    private ImageButton btnScanner;

    private TextInputLayout etProductNameWrap;
    private TextInputLayout etProductNumberWrap;
    private TextInputLayout etUPCWrap;
    private TextInputLayout etDescriptionWrap;
    private TextInputLayout etAmountWrap;
    private TextInputLayout etPriceWrap;

    private ProductFormValidatorHelper validator;

    private Inventory inventory;
    private Product product;

    private boolean edit = false;

    private String loadUPC = "";

    private boolean initialized = false;

    public CreateProductFragment() {
        // Required empty public constructor
    }
    public static CreateProductFragment newInstance() {
        CreateProductFragment fragment = new CreateProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static CreateProductFragment newInstance(Inventory inventory, boolean edit) {
        CreateProductFragment fragment = new CreateProductFragment();
        Bundle args = new Bundle();
        args.putParcelable("inventory", inventory);
        args.putBoolean("edit", edit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            inventory = getArguments().getParcelable("inventory");
            edit = getArguments().getBoolean("edit");
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etProductName = (TextInputEditText)view.findViewById(R.id.etProductName);
        etProductNumber = (TextInputEditText)view.findViewById(R.id.etProductNumber);
        etUPC = (TextInputEditText)view.findViewById(R.id.etUPC);
        etDescription = (TextInputEditText)view.findViewById(R.id.etDescription);
        etAmount = (TextInputEditText)view.findViewById(R.id.etAmount);
        etPrice = (TextInputEditText)view.findViewById(R.id.etPrice);

        btnScanner = (ImageButton)view.findViewById(R.id.btnScanner);
        btnScanner.setOnClickListener(btnScannerListener);

        etProductNameWrap = (TextInputLayout)view.findViewById(R.id.etProductNameWrap);
        etProductNumberWrap = (TextInputLayout)view.findViewById(R.id.etProductNumberWrap);
        etUPCWrap = (TextInputLayout)view.findViewById(R.id.etUPCWrap);
        etDescriptionWrap = (TextInputLayout)view.findViewById(R.id.etDescriptionWrap);
        etAmountWrap = (TextInputLayout)view.findViewById(R.id.etAmountWrap);
        etPriceWrap = (TextInputLayout)view.findViewById(R.id.etPriceWrap);
        etPriceWrap = (TextInputLayout)view.findViewById(R.id.etPriceWrap);

        validator = new ProductFormValidatorHelper();

    }

    @Override
    public void onResume() {
        super.onResume();

        if(loadUPC != ""){
            etUPC.setText(loadUPC);
        }

        if(inventory != null){
            inventory.printObject();
            setData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_product, container, false);
    }

    public void setUPC(String upc){
        Inventory inv = Inventory.get(upc);
        if(inv != null){
            inventory = inv;
        }else{
            loadUPC = upc;
        }
    }

    private void setData(){
        etProductName.setText(inventory.getProduct().getName());
        etProductNumber.setText(inventory.getProduct().getNumber());
        etUPC.setText(inventory.getProduct().getUpc());
        etDescription.setText(inventory.getProduct().getDescription());
        if(edit){
            etAmount.setText(String.valueOf(inventory.getAmount()));
        }
        if(inventory.getProduct().getPrice() != 0)
            etPrice.setText(String.valueOf(inventory.getProduct().getPrice()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("dsada", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        Log.d("sdad", "Attached");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create_product_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_submit) {

            if(inventory == null) {
                product = new Product();
            }else{
                product = inventory.getProduct();
            }

            product.setName(etProductName.getText().toString());
            product.setNumber(etProductNumber.getText().toString().toUpperCase());
            product.setUpc(etUPC.getText().toString());
            product.setDescription(etDescription.getText().toString());

            //Name field validation
            if(!validator.validateProductName(product.getName())){ return false; }
            //Number field validation
            if(!validator.validateProductNumber(product.getNumber())){ return false; }
            //UPC field validation
            if(!validator.validateProductUPC(product.getUpc())) { return false; }
            //Price field validation
            if(!validator.validatePrice(etPrice.getText().toString())) {
                return  false;
            }else{
                product.setPrice(Double.parseDouble(etPrice.getText().toString()));
            }
            //Amount field validation
            if(!validator.validateAmount(etAmount.getText().toString())) {
                return  false;
            }else{
                if(inventory == null) {
                    inventory = new Inventory(product, Integer.parseInt(etAmount.getText().toString()));
                }else{
                    if(edit) inventory.setAmount(Integer.parseInt(etAmount.getText().toString()));
                    else inventory.setAmount(inventory.getAmount() + Integer.parseInt(etAmount.getText().toString()));
                }

                inventory.save();
            }

            getActivity().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener btnScannerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.onFragmentInteraction(Uri.parse(Const.SCANNER_FRAGMENT_FROM_CREATE_PRODUCT));
        }
    };


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class ProductFormValidatorHelper extends InputValidationHelper {

        public boolean validateProductName(String name){
            if(isNullOrEmpty(name)){
                etProductNameWrap.setError("Enter product name");
                requestFocus(etProductName);
                return false;
            }
            etProductNameWrap.setErrorEnabled(false);
            return true;
        }

        public boolean validateProductNumber(String number){
            if(isNullOrEmpty(number)){
                etProductNumberWrap.setError("Enter product number");
                requestFocus(etProductNumber);
                return false;
            }else if(withSpace(number)){
                etProductNumberWrap.setError("Product number can't contain spaces");
                requestFocus(etProductNumber);
                return false;
            }
            etProductNumberWrap.setErrorEnabled(false);
            return true;
        }

        public boolean validateProductUPC(String upc){
            if(isNullOrEmpty(upc)){
                etUPCWrap.setError("Enter product UPC");
                requestFocus(etUPC);
                return false;
            }else if(!isNumeric(upc)){
                etUPCWrap.setError("UPC should be numeric");
                requestFocus(etUPC);
                return false;
            }
            etUPCWrap.setErrorEnabled(false);
            return true;
        }

        public boolean validatePrice(String price){
            double priceD = 0;
            try {
                priceD = Double.parseDouble(etPrice.getText().toString());
            }catch (Exception ex){
                etPriceWrap.setError("Price should be numeric");
                requestFocus(etPrice);
                return false;
            }

            if(priceD <= 0){
                etPriceWrap.setError("Price should be numeric");
                requestFocus(etPrice);
                return false;
            }
            etPriceWrap.setErrorEnabled(false);
            return true;
        }

        public boolean validateAmount(String amount){
            int amountI = 0;
            try {
                amountI = Integer.parseInt(amount);
            }catch (Exception ex){
                etAmountWrap.setError("Amount should be numeric");
                requestFocus(etAmount);
                return false;
            }

            if(amountI <= 0) {
                etAmountWrap.setError("Amount can't be 0 or less");
                requestFocus(etAmount);
                return false;
            }
            etAmountWrap.setErrorEnabled(false);
            return true;
        }

        private void requestFocus(View view) {
            if (view.requestFocus()) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }
    }
}
