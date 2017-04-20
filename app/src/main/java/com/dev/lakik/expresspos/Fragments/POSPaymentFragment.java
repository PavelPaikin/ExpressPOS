package com.dev.lakik.expresspos.Fragments;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dev.lakik.expresspos.Helpers.InputValidationHelper;
import com.dev.lakik.expresspos.Model.Company;
import com.dev.lakik.expresspos.Model.Const;
import com.dev.lakik.expresspos.Model.Transaction;
import com.dev.lakik.expresspos.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link POSPaymentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link POSPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class POSPaymentFragment extends Fragment {

        private OnFragmentInteractionListener mListener;

    public POSPaymentFragment() {
        // Required empty public constructor
    }


    public static POSPaymentFragment newInstance(Transaction transaction) {
        POSPaymentFragment fragment = new POSPaymentFragment();
        Bundle args = new Bundle();
        args.putParcelable("transaction", transaction);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transaction = getArguments().getParcelable("transaction");
        }

        if (transaction == null) {
            transaction = new Transaction(Company.instance.getId());
        }
    }

    private Transaction transaction;

    InputValidationHelper validator;

    EditText nameET, cardNumberET, monthET, yearET, cvvET;
    TextInputLayout nameETWrap, cardNumberETWrap, monthETWrap, yearETWrap, cvvETWrap;

    ArrayList<TextInputLayout> wrapList;

    ImageView brandImg;

    String name, cardNumber, month, year, cvv;

    Button submitBtn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_pospayment, container, false);


        nameET = (EditText) view.findViewById(R.id.pay_nameET);

        cardNumberET = (EditText) view.findViewById(R.id.pay_cardNumberET);

        nameETWrap = (TextInputLayout) view.findViewById(R.id.pay_nameETWrap);
        cardNumberETWrap = (TextInputLayout) view.findViewById(R.id.pay_cardNumberETWrap);
        monthETWrap = (TextInputLayout) view.findViewById(R.id.pay_monthETWrap);
        yearETWrap = (TextInputLayout) view.findViewById(R.id.pay_yearETWrap);
        cvvETWrap = (TextInputLayout) view.findViewById(R.id.pay_cvvETWrap);

        wrapList = new ArrayList<>();
        wrapList.add(nameETWrap);
        wrapList.add(cardNumberETWrap);
        wrapList.add(monthETWrap);
        wrapList.add(yearETWrap);
        wrapList.add(cvvETWrap);



        monthET = (EditText) view.findViewById(R.id.pay_monthET);
        yearET = (EditText) view.findViewById(R.id.pay_yearET);
        cvvET = (EditText) view.findViewById(R.id.pay_cvvET);
        brandImg = (ImageView) view.findViewById(R.id.pay_brandImg);

        submitBtn = (Button) view.findViewById(R.id.pay_submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validEntry = true;

                for (TextInputLayout wrap : wrapList) {
                    if (wrap.getEditText().getText().toString().equals("")) {
                        wrap.setErrorEnabled(true);
                        wrap.setError("Field must be filled");
                        validEntry = false;
                    } else {
                        wrap.setErrorEnabled(false);
                    }
                }

                if (cardNumberET.getText().toString().length() < 16) {
                    cardNumberETWrap.setErrorEnabled(true);
                    cardNumberETWrap.setError("Card number must be at least 16 digits");
                }

                if (validEntry) {
                    transaction.save();
                    transaction.removeAllFromInventory();
                    mListener.onFragmentInteraction(Uri.parse(Const.SUMMARY_FRAGMENT_FROM_PAYMENT));
                }
            }
        });



        cardNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String tempNum = cardNumberET.getText().toString();

                if (tempNum.startsWith("4")) { //visa
                    brandImg.setImageResource(R.drawable.visa);
                } else if (tempNum.startsWith("5") || tempNum.startsWith("2")) { //mastercard
                    brandImg.setImageResource(R.drawable.mastercard);
                } else if (tempNum.startsWith("34") || tempNum.startsWith("37")) { //amex
                    brandImg.setImageResource(R.drawable.amex);
                } else {
                    brandImg.setImageResource(R.drawable.ic_credit_card_black_24dp);
                }

                if (tempNum.length() > 19) {
                    tempNum = tempNum.substring(0, tempNum.length() - 1);
                    cardNumberET.setText(tempNum);
                    cardNumberET.setSelection(tempNum.length());
                    cardNumberETWrap.setError("Must be no more than 19 characters");
                } else {
                    cardNumberETWrap.setErrorEnabled(false);
                }


            }



            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        cvvET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tempNum = cvvET.getText().toString();
                if (tempNum.length() > 4) {
                    tempNum = tempNum.substring(0, tempNum.length() - 1);
                    cvvET.setText(tempNum);
                    cvvET.setSelection(tempNum.length());
                    cvvETWrap.setError("Must be no more than 4 digits");
                } else {
                    cvvETWrap.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        monthET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tempNum = monthET.getText().toString();
                if (tempNum.length() > 2) {
                    tempNum = tempNum.substring(0, tempNum.length() - 1);
                    monthET.setText(tempNum);
                    monthET.setSelection(tempNum.length());
                    monthETWrap.setError("Must be no more than 2 digits");
                } else {
                    monthETWrap.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        yearET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tempNum = yearET.getText().toString();
                if (tempNum.length() > 4) {
                    tempNum = tempNum.substring(0, tempNum.length() - 1);
                    yearET.setText(tempNum);
                    yearET.setSelection(tempNum.length());
                    yearETWrap.setError("Must be no more than 4 digits");
                } else {
                    yearETWrap.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListener.setToolbarTitle("Total: $" + transaction.getTotal());
    }

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

    //Submit button menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create_product_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_submit) {
            boolean validEntry = true;

            for (TextInputLayout wrap : wrapList) {
                if (wrap.getEditText().getText().toString().equals("")) {
                    wrap.setErrorEnabled(true);
                    wrap.setError("Field must be filled");
                    validEntry = false;
                } else {
                    wrap.setErrorEnabled(false);
                }
            }

            if (cardNumberET.getText().toString().length() < 16) {
                cardNumberETWrap.setErrorEnabled(true);
                cardNumberETWrap.setError("Card number must be at least 16 digits");
            }

            if (validEntry) {
                transaction.save();
                transaction.removeAllFromInventory();
                mListener.onFragmentInteraction(Uri.parse(Const.SUMMARY_FRAGMENT_FROM_PAYMENT));
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
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
        void setToolbarTitle(String title);
    }
}
