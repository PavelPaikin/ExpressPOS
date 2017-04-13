package com.dev.lakik.expresspos.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lakik.expresspos.Model.Transaction;
import com.dev.lakik.expresspos.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link POSSummaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link POSSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class POSSummaryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    Transaction transaction;


    public POSSummaryFragment() {
        // Required empty public constructor
    }


    public static POSSummaryFragment newInstance() {
        POSSummaryFragment fragment = new POSSummaryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public static POSSummaryFragment newInstance(Transaction transaction) {
        POSSummaryFragment fragment = new POSSummaryFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_possummary, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListener.setToolbarTitle("Summary");
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void setToolbarTitle(String title);
    }
}
