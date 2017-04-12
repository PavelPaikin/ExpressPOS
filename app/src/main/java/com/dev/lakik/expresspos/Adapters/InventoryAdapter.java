package com.dev.lakik.expresspos.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.lakik.expresspos.Model.Inventory;
import com.dev.lakik.expresspos.Model.Product;
import com.dev.lakik.expresspos.R;

import java.util.ArrayList;

/**
 * Created by ppash on 10.04.2017.
 */

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private ArrayList<Inventory> mDataset;

    public InventoryAdapter() {
        mDataset = new ArrayList<Inventory>();
    }

    @Override
    public InventoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_inventory, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mDataset.get(position).getProduct().getName());
        holder.tvNumber.setText(mDataset.get(position).getProduct().getNumber());
        holder.tvAmount.setText(mDataset.get(position).getAmount() + "");
        holder.tvPrice.setText(String.format("%.2f$", mDataset.get(position).getProduct().getPrice()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Inventory getItem(int positon){
        return mDataset.get(positon);
    }

    public void setData(ArrayList<Inventory> myDataset) {
        mDataset = myDataset;
        notifyDataSetChanged();
    }

    public void removeItem(int positon){
        mDataset.get(positon).delete();
        mDataset.remove(positon);
        notifyItemRemoved(positon);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvNumber;
        public TextView tvAmount;
        public TextView tvPrice;
        public ImageView imgProduct;

        public ViewHolder(View v) {
            super(v);
            tvName = (TextView)v.findViewById(R.id.tvProductName);
            tvNumber = (TextView)v.findViewById(R.id.tvProductNumber);
            tvAmount = (TextView)v.findViewById(R.id.tvAmount);
            tvPrice = (TextView)v.findViewById(R.id.tvPrice);
            imgProduct = (ImageView)v.findViewById(R.id.imgProduct);
        }
    }
}
