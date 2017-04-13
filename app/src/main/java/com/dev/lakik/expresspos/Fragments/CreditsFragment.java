package com.dev.lakik.expresspos.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lakik.expresspos.Adapters.CreditsAdapter;
import com.dev.lakik.expresspos.Adapters.OrdersAdapter;
import com.dev.lakik.expresspos.R;

public class CreditsFragment extends Fragment {

    private RecyclerView rvCredits;
    private CreditsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public CreditsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_credits, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCredits = (RecyclerView) view.findViewById(R.id.rvCredits);
        mLayoutManager = new LinearLayoutManager(getContext());
        rvCredits.setLayoutManager(mLayoutManager);

        mAdapter = new CreditsAdapter(getContext());
        rvCredits.setAdapter(mAdapter);

        rvCredits.setNestedScrollingEnabled(false);
    }
}
