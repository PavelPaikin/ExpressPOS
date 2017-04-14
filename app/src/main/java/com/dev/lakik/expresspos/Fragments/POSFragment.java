package com.dev.lakik.expresspos.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Database.ModelHelpers.ProductHelper;
import com.dev.lakik.expresspos.MainActivity;
import com.dev.lakik.expresspos.Model.Const;
import com.dev.lakik.expresspos.Model.Inventory;
import com.dev.lakik.expresspos.Model.Product;
import com.dev.lakik.expresspos.Model.Transaction;
import com.dev.lakik.expresspos.Model.TransactionProduct;
import com.dev.lakik.expresspos.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class POSFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FloatingActionMenu famPOS;

    private float tax = 0.13f;

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

    /*
        Basic product information meant to be used for the receipt
        Contains the product price, number purchased, and product ID
     */
    ArrayList<TransactionProduct> transactionProdArray = new ArrayList<>();

    /*
        Contains products that are added to the transaction, but only temporarily
        This is meant to populate the cardview, but should not be referenced for the price in the log of the transaction
        This should only be used for the product name after the transaction is submitted, using the product ID stored in the transactionProdArray
            to get the product name, UPC etc, since the product's price is likely to change frequently
     */
    ArrayList<Product> prodArray = new ArrayList<>();

    /*
        Contains the current inventory information
        Holds an array of products that represent the items which are currently available to add to the transaction
     */
    ArrayList<Inventory> inventoryArray = new ArrayList<>();

    RecyclerView rv;

    TextView subtotalNumTV, taxNumTV, totalNumTV;

    ListView listView;

    Button submitBtn;

    Transaction thisTrans;

    double subtotal;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_pos, container, false);

        subtotalNumTV = (TextView) view.findViewById(R.id.pos_subtotalNumTV);
        taxNumTV = (TextView) view.findViewById(R.id.pos_taxNumTV);
        totalNumTV = (TextView) view.findViewById(R.id.pos_totalNumTV);

        inventoryArray = Inventory.getAllRecords();//Populate the inventory array from the database
        thisTrans = new Transaction();

        //RecyclerView for the product CardView
        rv = (RecyclerView) view.findViewById(R.id.pos_recyclerView);
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        final RVAdapter adapter = new RVAdapter(prodArray);
        rv.setAdapter(adapter);
        calculateTotal();

        famPOS = (FloatingActionMenu)view.findViewById(R.id.pos_fam);


        FloatingActionButton fabScanProduct = (FloatingActionButton)view.findViewById(R.id.fab_scan_product);
        fabScanProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(Uri.parse(Const.SCANNER_FRAGMENT_FROM_POS));
            }
        });


        FloatingActionButton fabSelectFromList = (FloatingActionButton)view.findViewById(R.id.fab_select_from_list);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(sharedPrefs.getBoolean("key_show_list_button", true)) {

            fabSelectFromList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Alert Dialog and ListView
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    inventoryArray.clear();
                    inventoryArray = Inventory.getAllRecords();//Populate the inventory array from the database
                    alertDialog.setTitle("Add Product");
                    final LVAdapter lvAdapter = new LVAdapter(getContext(), inventoryArray);
                    View newView = LayoutInflater.from(getContext()).inflate(R.layout.pos_addlist, container, false);
                    listView = (ListView) newView.findViewById(R.id.pos_addListView);
                    listView.setAdapter(lvAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Product newProduct = inventoryArray.get(i).getProduct();
//                            prodArray.add(newProduct);
                            thisTrans.addProduct(newProduct);
                            prodArray.add(newProduct);
                            //prodArray = thisTrans.getProducts();
                            alertDialog.cancel();
                            adapter.notifyDataSetChanged();
                            calculateTotal();

                            famPOS.close(true);
                        }
                    });

                    alertDialog.setView(listView);
                    alertDialog.show();
                }
            });

        }else{
            famPOS.removeMenuButton(fabSelectFromList);
        }

        submitBtn = (Button) view.findViewById(R.id.pos_submit);

        submitBtn.setOnClickListener(new View.OnClickListener() { // TODO: 4/12/2017 Make this a button in the toolbar
            @Override
            public void onClick(View view) {

                calculateTotal();

                float tSub, tTax, tTotal;
                tSub = (float) subtotal;
                tTax = (float) round(subtotal * tax, 2);
                tTotal = (float) round(subtotal * (1 + tax), 2);

                thisTrans.setSubTotal(tSub);
                thisTrans.setTax(tTax);
                thisTrans.setTotal(tTotal);
                //thisTrans.save();
                System.out.println("Product Count: " + thisTrans.getProductsCount());

                mListener.onFragmentInteraction(Uri.parse(Const.PAYMENT_FRAGMENT_FROM_POS));

            }
            });


        return view;
    }

    public Transaction getTransaction() {
        return thisTrans;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListener.setToolbarTitle("POS");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        tax = Float.parseFloat(sharedPrefs.getString("key_tax_value", "13")) / 100;
    }

    private void calculateTotal() {
        subtotal = 0;
        for (Product product : prodArray) {
            subtotal += product.getPrice();
        }

        String total = "$" + round(subtotal, 2);
        subtotalNumTV.setText(total);

        total = "$" + round(subtotal * tax, 2);
        taxNumTV.setText(total);

        total = "$" + round(subtotal * (1 + tax), 2);
        totalNumTV.setText(total);

    }

    public void addProduct(Product product){
        //prodArray.add(product);
        thisTrans.addProduct(product);
        calculateTotal();
        famPOS.close(true);
    }

    /*
        From: http://stackoverflow.com/a/2808648
     */
    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public class LVAdapter extends ArrayAdapter<Inventory> {
        public LVAdapter(Context context, ArrayList<Inventory> invs) {
            super(context, 0, invs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Inventory inventory = getItem(position);
            Product product = inventory.getProduct();

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.pos_addlistitem, parent, false);
            }

            TextView nameTV = (TextView) convertView.findViewById(R.id.pos_addListName);
            TextView priceTV = (TextView) convertView.findViewById(R.id.pos_addListPrice);

            nameTV.setText(product.getName());
            String priceStr = "$" + product.getPrice();
            priceTV.setText(priceStr);


            return convertView;
        }

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
            pvh.countET.setText("1");


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


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.create_product_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//
//        return true;
//    }


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
        void setToolbarTitle(String title);
    }
}
