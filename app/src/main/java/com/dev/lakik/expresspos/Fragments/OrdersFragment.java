package com.dev.lakik.expresspos.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lakik.expresspos.Adapters.InventoryAdapter;
import com.dev.lakik.expresspos.Adapters.OrdersAdapter;
import com.dev.lakik.expresspos.Listeners.RecyclerItemClickListener;
import com.dev.lakik.expresspos.Model.Company;
import com.dev.lakik.expresspos.Model.Transaction;
import com.dev.lakik.expresspos.R;

import java.util.ArrayList;
import java.util.Date;

import static com.dev.lakik.expresspos.R.id.rvInventory;


public class OrdersFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener{

    private OnFragmentInteractionListener mListener;

    private RecyclerView rvOrders;
    private OrdersAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Transaction> arr;

    public OrdersFragment() {}

    public static OrdersFragment newInstance() {
        OrdersFragment fragment = new OrdersFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvOrders = (RecyclerView) view.findViewById(R.id.rvOrders);
        mLayoutManager = new LinearLayoutManager(getContext());
        rvOrders.setLayoutManager(mLayoutManager);

        mAdapter = new OrdersAdapter(getContext());
        rvOrders.setAdapter(mAdapter);

        rvOrders.setNestedScrollingEnabled(false);
        rvOrders.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), this));

        mListener.setToolbarTitle("Orders");
    }

    @Override
    public void onResume() {
        super.onResume();

        arr = Transaction.getAllRecords(Company.instance.getId());
        mAdapter.setData(arr);
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

    @Override
    public void onItemClick(View childView, int position) {
        mListener.showTransactionSummary(arr.get(position));
    }

    @Override
    public void onItemLongPress(View childView, int position) {
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void showTransactionSummary(Transaction transaction);
        void setToolbarTitle(String title);
    }
}
