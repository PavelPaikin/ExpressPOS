package com.dev.lakik.expresspos.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.lakik.expresspos.Model.Product;
import com.dev.lakik.expresspos.Model.Transaction;
import com.dev.lakik.expresspos.Model.TransactionProduct;
import com.dev.lakik.expresspos.R;

import java.util.ArrayList;

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

    RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_possummary, container, false);

        rv = (RecyclerView) view.findViewById(R.id.summ_recyclerView);
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        final RVAdapter adapter = new RVAdapter(transaction.getProducts());
        rv.setAdapter(adapter);



        return view;
    }


    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.ProductViewHolder> {

        ArrayList<TransactionProduct> transactionProducts;

        RVAdapter(ArrayList<TransactionProduct> transactionProducts) {
            this.transactionProducts = transactionProducts;
        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {

            CardView cv;
            TextView prodNameTV, priceTV;

            ProductViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.summary_cardview);
                prodNameTV = (TextView) itemView.findViewById(R.id.summ_prodNameTV);
                priceTV = (TextView) itemView.findViewById(R.id.summ_priceTV);

            }

        }

        @Override
        public int getItemCount() {
            return transactionProducts.size();
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pos_summary_card, viewGroup, false);
            ProductViewHolder pvh = new ProductViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(ProductViewHolder pvh, int i) {
            TransactionProduct transProd = transactionProducts.get(i);
            Product prod = transProd.getProduct();

            pvh.prodNameTV.setText(prod.getName());

            String priceString = transProd.getPrice() + "";

            pvh.priceTV.setText(priceString);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mListener.setToolbarTitle("Summary");
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
