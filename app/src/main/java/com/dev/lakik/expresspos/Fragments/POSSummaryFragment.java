package com.dev.lakik.expresspos.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
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
            transaction.save();
        }
    }

    RecyclerView rv;
    TextView subTV, taxTV, totalTV;
    ImageView sendImg;

    float subtotal, tax, total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_possummary, container, false);

        //Setup recyclerview
        rv = (RecyclerView) view.findViewById(R.id.summ_recyclerView);
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        final RVAdapter adapter = new RVAdapter(transaction.getProducts());
        rv.setAdapter(adapter);

        //Set bottom displays for numbers
        subTV = (TextView) view.findViewById(R.id.summ_subNumTV);
        taxTV = (TextView) view.findViewById(R.id.summ_taxNumTV);
        totalTV = (TextView) view.findViewById(R.id.summ_totalNumTV);

        subtotal = transaction.getSubTotal();
        String subtotalStr = "$" + subtotal;
        subTV.setText(subtotalStr);

        tax = transaction.getTax();
        String taxStr = "$" + tax;
        taxTV.setText(taxStr);

        total = transaction.getTotal();
        String totalStr = "$" + total;
        totalTV.setText(totalStr);

        //Share intent to share the receipt

        sendImg = (ImageView) view.findViewById(R.id.summ_sendImg);
        sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = "";

                shareBody += "Date: " + transaction.getDate() + "\n\n";

                for (TransactionProduct p : transaction.getProducts()) {
                    if (p.getAmount() > 1) {
                        shareBody += p.getAmount() + "x " + p.getProduct().getName() + " at $" + p.getPrice() + " = $" + (p.getPrice() * p.getAmount()) + "\n";
                    } else {
                        shareBody += p.getProduct().getName() + " = $" + p.getPrice() + "\n";
                    }
                }

                shareBody += "\nSubtotal: $" + transaction.getSubTotal();
                shareBody += "\nTax: $" + transaction.getTax();
                shareBody += "\nTotal: $" + transaction.getTotal();

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Purchase $" + transaction.getTotal() + " on " + transaction.getDate());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using..."));
            }
        });



        return view;
    }


    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.ProductViewHolder> {

        ArrayList<TransactionProduct> transactionProducts;

        RVAdapter(ArrayList<TransactionProduct> transactionProducts) {
            this.transactionProducts = transactionProducts;
        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {

            CardView cv;
            TextView prodNameTV, upcTV, countTV, unitPriceTV, totalPriceTV;

            ProductViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.summary_cardview);
                prodNameTV = (TextView) itemView.findViewById(R.id.summ_prodNameTV);
                upcTV = (TextView) itemView.findViewById(R.id.summ_upcTV);
                countTV = (TextView) itemView.findViewById(R.id.summ_countTV);
                unitPriceTV = (TextView) itemView.findViewById(R.id.summ_unitPriceTV);
                totalPriceTV = (TextView) itemView.findViewById(R.id.summ_totalPriceTV);

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
            double unitPrice = transProd.getPrice();
            int unitCount = transProd.getAmount();

            pvh.prodNameTV.setText(prod.getName());

            pvh.upcTV.setText(prod.getUpc());

            String countString = "x" + unitCount;
            pvh.countTV.setText(countString);

            String unitPriceString = "$" + unitPrice;
            pvh.unitPriceTV.setText(unitPriceString);

            String totalPriceString = "= $" + (unitPrice * unitCount);
            pvh.totalPriceTV.setText(totalPriceString);

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
        //void setToolbarTitle(String title);
    }
}
