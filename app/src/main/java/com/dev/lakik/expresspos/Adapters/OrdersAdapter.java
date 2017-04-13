package com.dev.lakik.expresspos.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.lakik.expresspos.Model.Transaction;
import com.dev.lakik.expresspos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ppash on 13.04.2017.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private ArrayList<Transaction> mDataset;
    private Context ctx;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.CANADA);

    public OrdersAdapter(Context ctx) {
        this.ctx = ctx;
        mDataset = new ArrayList<Transaction>();
    }

    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_orders, parent, false);
        return new OrdersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrdersAdapter.ViewHolder holder, int position) {
        holder.tvNumber.setText(String.valueOf(getItemCount() - position));
        holder.tvDate.setText(dateFormat.format(mDataset.get(position).getDate()));
        holder.tvItemSold.setText(String.valueOf(mDataset.get(position).getProductsCount()));
        holder.tvTotal.setText(String.format(Locale.CANADA, "%.2f$", mDataset.get(position).getTotal()));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Transaction getItem(int positon){
        return mDataset.get(positon);
    }

    public void setData(ArrayList<Transaction> myDataset) {
        mDataset = myDataset;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNumber;
        private TextView tvDate;
        private TextView tvItemSold;
        private TextView tvTotal;

        private ViewHolder(View v) {
            super(v);

            tvNumber = (TextView)v.findViewById(R.id.tvTransactionNumber);
            tvDate = (TextView)v.findViewById(R.id.tvTransactionDate);
            tvItemSold = (TextView)v.findViewById(R.id.tvItemCount);
            tvTotal = (TextView)v.findViewById(R.id.tvTransactionTotal);
        }
    }
}
