package com.dev.lakik.expresspos.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dev.lakik.expresspos.Adapters.InventoryAdapter;
import com.dev.lakik.expresspos.Listeners.RecyclerItemClickListener;
import com.dev.lakik.expresspos.Model.Const;
import com.dev.lakik.expresspos.Model.Inventory;
import com.dev.lakik.expresspos.Model.Product;
import com.dev.lakik.expresspos.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class InventoryFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener{

    private ZXingScannerView mScannerView;

    private OnFragmentInteractionListener mListener;
    CoordinatorLayout coordinator;

    private RecyclerView rvInventory;
    private InventoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public InventoryFragment() {}

    public static InventoryFragment newInstance() {
        InventoryFragment fragment = new InventoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coordinator = (CoordinatorLayout)view.findViewById(R.id.inventory_coordinator);

        FloatingActionButton fabScanProduct = (FloatingActionButton)view.findViewById(R.id.fab_scan_product);
        fabScanProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(Uri.parse(Const.SCANNER_FRAGMENT_FROM_INVENTORY));
            }
        });

        FloatingActionButton fabCreateNewProduct = (FloatingActionButton)view.findViewById(R.id.fab_create_new_product);
        fabCreateNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(Uri.parse(Const.CREATE_NEW_PRODUCT_FRAGMENT));
            }
        });

        rvInventory = (RecyclerView) view.findViewById(R.id.rvInventory);
        rvInventory.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        rvInventory.setLayoutManager(mLayoutManager);

        mAdapter = new InventoryAdapter(getContext());
        rvInventory.setAdapter(mAdapter);

        rvInventory.setNestedScrollingEnabled(false);
        rvInventory.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
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
    public void onResume() {
        super.onResume();

        mAdapter.setData(Inventory.getAllRecords());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(View childView, int position) {
        Log.d("dsad", "CLICKED");
        mListener.editProduct(mAdapter.getItem(position), true);

    }

    @Override
    public void onItemLongPress(View childView, int position) {
        Log.d("dsad", "LONG");
        mAdapter.removeItem(position);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void editProduct(Inventory inventory, boolean edit);
    }
}
