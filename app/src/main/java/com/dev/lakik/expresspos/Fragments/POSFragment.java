package com.dev.lakik.expresspos.Fragments;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.lakik.expresspos.Model.Product;
import com.dev.lakik.expresspos.R;

import java.util.ArrayList;

public class POSFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public POSFragment() {
        // Required empty public constructor
    }


    public static POSFragment newInstance() {
        POSFragment fragment = new POSFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    ArrayList<Product> prodArray = new ArrayList<>();
    RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pos, container, false);

        //Don't brute force products, this is for testing. Array list should be populated with the database
        prodArray.add(new Product("Planters Peanuts", "102746", "05871697076", "Dry roasted peanuts", 2.99));
        prodArray.add(new Product("No Name Peanuts", "487576", "46721828384", "Cheaper dry roasted peanuts", 1.99));
        //End Testing code

        rv = (RecyclerView) view.findViewById(R.id.pos_recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        RVAdapter adapter = new RVAdapter(prodArray);
        rv.setAdapter(adapter);

        return view;
    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ProductViewHolder> {

        ArrayList<Product> products;

        RVAdapter(ArrayList<Product> products) {
            this.products = products;
        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            ImageView prodImg;
            TextView prodName, prodUPC, prodPrice;
            Button addBtn, minusBtn;
            EditText countET;

            ProductViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.pos_cardView);
                prodImg = (ImageView) itemView.findViewById(R.id.pos_prodImg);
                prodName = (TextView) itemView.findViewById(R.id.pos_prodName);
                prodUPC = (TextView) itemView.findViewById(R.id.pos_prodUpc);
                prodPrice = (TextView) itemView.findViewById(R.id.pos_prodPrice);
                addBtn = (Button) itemView.findViewById(R.id.pos_addBtn);
                minusBtn = (Button) itemView.findViewById(R.id.pos_minusBtn);
                countET = (EditText) itemView.findViewById(R.id.pos_countET);
            }
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pos_prodcard, viewGroup, false);
            ProductViewHolder pvh = new ProductViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(ProductViewHolder pvh, int i) {
            pvh.prodName.setText(products.get(i).getName());
            pvh.prodUPC.setText(products.get(i).getUpc());
            String priceStr = "$" + products.get(i).getPrice();
            pvh.prodPrice.setText(priceStr);


        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

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
        void onFragmentInteraction(Uri uri);
    }
}
