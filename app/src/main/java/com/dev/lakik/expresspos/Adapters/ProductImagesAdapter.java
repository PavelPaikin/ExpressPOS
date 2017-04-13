package com.dev.lakik.expresspos.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.dev.lakik.expresspos.Fragments.ProductImageFragment;
import com.dev.lakik.expresspos.Model.ProductImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ppash on 12.04.2017.
 */

public class ProductImagesAdapter extends FragmentStatePagerAdapter {

    FragmentManager fm;
    ArrayList<ProductImage> images;

    public ProductImagesAdapter(FragmentManager fm) {
        super(fm);

        this.fm = fm;
        images = new ArrayList<>();
    }

    public void setData(ArrayList<ProductImage> data){
        images = data;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return ProductImageFragment.newInstance(images.get(position));
    }


    public int getItemPosition(Object item) {
        ProductImageFragment fragment = (ProductImageFragment)item;
        ProductImage image = fragment.getImage();
        int position = images.indexOf(image);

        if (position >= 0) {
            return position;
        } else {
            return POSITION_NONE;
        }
    }

    @Override
    public int getCount() {
        return images.size();
    }

}
