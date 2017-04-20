package com.dev.lakik.expresspos.Fragments;


import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dev.lakik.expresspos.Model.ProductImage;
import com.dev.lakik.expresspos.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductImageFragment extends Fragment {

    private ImageView image;
    private ProductImage imageObj;

    public static ProductImageFragment newInstance(ProductImage imageObj) {

        Bundle args = new Bundle();
        args.putParcelable("image", imageObj);

        ProductImageFragment fragment = new ProductImageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public ProductImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            imageObj = getArguments().getParcelable("image");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image = (ImageView)view.findViewById(R.id.imgProduct);
        image.setTag("product image");
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                ((CreateProductFragment)getParentFragment()).removeImage(imageObj.getId());
                /*ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new CustomDragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                v.setVisibility(View.INVISIBLE);
*/
                return true;
            }
        });
        image.setOnDragListener(new MyDragListener());

        Picasso.with(getContext()).load(new File(imageObj.getImagePath())).resize(400, 400).centerInside().into(image);
    }

    public ProductImage getImage() {
        return imageObj;
    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }

    public static class CustomDragShadowBuilder extends View.DragShadowBuilder {

        //private static final int SCALING_FACTOR = 0.2;

        public CustomDragShadowBuilder(View view) {
            super(view);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            View v = getView();
            float width = v.getWidth() * 0.4f;
            float height = v.getHeight() * 0.4f;
            shadowSize.set((int)width, (int)height);
            shadowTouchPoint.set((int)(width / 2), (int)(height / 2));
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            canvas.scale(0.4f, 0.4f);
            super.onDrawShadow(canvas);
        }



    }
}
