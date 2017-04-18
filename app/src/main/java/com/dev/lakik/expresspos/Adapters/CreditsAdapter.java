package com.dev.lakik.expresspos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.lakik.expresspos.Model.Credit;
import com.dev.lakik.expresspos.Model.Transaction;
import com.dev.lakik.expresspos.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ppash on 13.04.2017.
 */

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.ViewHolder> {
    private ArrayList<Credit> mDataset;
    private Context ctx;

    public CreditsAdapter(Context ctx) {
        this.ctx = ctx;
        mDataset = new ArrayList<Credit>();

        mDataset.add(new Credit(Credit.Source.GITHUB, "Clans FloatingActionButton", "Yet another implementation of Floating Action Button for Android with lots of features.", "https://github.com/Clans/FloatingActionButton"));
        mDataset.add(new Credit(Credit.Source.GITHUB, "Barcode Scanner Libraries", "Android library projects that provides easy to use and extensible Barcode Scanner views based on ZXing and ZBar.", "https://github.com/dm77/barcodescanner"));
        mDataset.add(new Credit(Credit.Source.GITHUB, "Picasso", "A powerful image downloading and caching library for Android", "https://github.com/square/picasso"));
        mDataset.add(new Credit(Credit.Source.GITHUB, "Crop images", "An Android library project that provides a simple image cropping Activity, based on code from AOSP.", "https://github.com/jdamcd/android-crop"));
        mDataset.add(new Credit(Credit.Source.GITHUB, "PageIndicatorView", "PageIndicatorView will simplify your life while you working with Android ViewPager and need to indicate selected page. It's easy to setup and customize as you need with run-time preview rendering.", "https://github.com/romandanylyk/PageIndicatorView"));
        mDataset.add(new Credit(Credit.Source.GITHUB, "CircleImageView", "A fast circular ImageView perfect for profile images. This is based on RoundedImageView from Vince Mi which itself is based on techniques recommended by Romain Guy.", "https://github.com/hdodenhof/CircleImageView"));
    }

    @Override
    public CreditsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_credits, parent, false);
        return new CreditsAdapter.ViewHolder(v);
    }


    Credit c;
    @Override
    public void onBindViewHolder(CreditsAdapter.ViewHolder holder, int position) {
        c = mDataset.get(position);
        int imgRes = R.drawable.git_hub;

        switch (c.getSource()){
            case GITHUB:
                imgRes = R.drawable.git_hub;
                break;
            case STACKOVERFLOW:
                imgRes = R.drawable.stackoverflow;
                break;
        }

        Picasso.with(ctx).load(imgRes).into(holder.imgSource);

        holder.tvTitle.setText(c.getTitle());
        holder.tvDescription.setText(c.getDescription());
        holder.btnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(c.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(ctx.getPackageManager()) != null) {
                    ctx.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Credit getItem(int positon){
        return mDataset.get(positon);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgSource;
        private TextView tvTitle;
        private TextView tvDescription;
        private Button btnVisit;

        private ViewHolder(View v) {
            super(v);

            imgSource = (ImageView) v.findViewById(R.id.imgSource);
            tvTitle = (TextView)v.findViewById(R.id.tvTitle);
            tvDescription = (TextView)v.findViewById(R.id.tvDescription);
            btnVisit = (Button) v.findViewById(R.id.btnVisit);
        }
    }
}
