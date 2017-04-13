package com.dev.lakik.expresspos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dev.lakik.expresspos.Fragments.LoginFragment;
import com.dev.lakik.expresspos.Fragments.RegisterFragment;
import com.dev.lakik.expresspos.Fragments.SplashFragment;

import com.dev.lakik.expresspos.CustomView.ControllableAppBarLayout;
import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Fragments.CreateProductFragment;
import com.dev.lakik.expresspos.Fragments.FullScannerFragment;
import com.dev.lakik.expresspos.Fragments.InventoryFragment;
import com.dev.lakik.expresspos.Fragments.OrdersFragment;
import com.dev.lakik.expresspos.Fragments.POSFragment;
import com.dev.lakik.expresspos.Model.Const;
import com.dev.lakik.expresspos.Model.Inventory;
import com.dev.lakik.expresspos.Model.Product;
import com.dev.lakik.expresspos.Model.ProductImage;
import com.soundcloud.android.crop.Crop;

import java.util.UUID;

import static java.lang.System.in;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoginFragment.OnFragmentInteractionListener,
        SplashFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        InventoryFragment.OnFragmentInteractionListener,
        CreateProductFragment.OnFragmentInteractionListener,
        POSFragment.OnFragmentInteractionListener,
        OrdersFragment.OnFragmentInteractionListener,
        FullScannerFragment.OnFragmentInteractionListener{

    CollapsingToolbarLayout collapsingToolbarLayout = null;
    ControllableAppBarLayout appBarLayout = null;
    Fragment currentFragment;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DBHelper(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsingToolbar);

        appBarLayout = (ControllableAppBarLayout)findViewById(R.id.controlledAppBar);
        appBarLayout.collapseToolbar();
        appBarLayout.lockToolbar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new SplashFragment()).commit();

        changeFragment(POSFragment.newInstance(), false);
        collapsingToolbarLayout.setTitle("POS");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            if(currentFragment instanceof CreateProductFragment) {
                appBarLayout.collapseToolbar();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        currentFragment.onActivityResult(requestCode, resultCode, data);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_pos:
                changeFragment(POSFragment.newInstance(), false);
                appBarLayout.collapseToolbar();
                collapsingToolbarLayout.setTitle("POS");
                break;
            case R.id.nav_inventory:
                changeFragment(InventoryFragment.newInstance(), false);
                appBarLayout.collapseToolbar();
                collapsingToolbarLayout.setTitle("Inventory");
                break;
            case R.id.nav_orders:
                changeFragment(OrdersFragment.newInstance(), false);
                appBarLayout.collapseToolbar();
                collapsingToolbarLayout.setTitle("Orders");
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeFragment(Fragment fragment, boolean addToStack){

        currentFragment = fragment;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if(fragment!=null) {
            if(addToStack) transaction.addToBackStack(null);
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            transaction.replace(R.id.mainContainer, fragment);
            transaction.commit();
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        switch (uri.toString()){
            case Const.CREATE_NEW_PRODUCT_FRAGMENT:
                changeFragment(CreateProductFragment.newInstance(), true);
                appBarLayout.collapseToolbar();
                collapsingToolbarLayout.setTitle("Add Product");
                break;
            case Const.SCANNER_FRAGMENT_FROM_INVENTORY:
                changeFragment(FullScannerFragment.newInstance(InventoryFragment.class.getName()), true);
                appBarLayout.collapseToolbar();
                collapsingToolbarLayout.setTitle("");
                break;
            case Const.SCANNER_FRAGMENT_FROM_CREATE_PRODUCT:
                changeFragment(FullScannerFragment.newInstance(CreateProductFragment.class.getName()), true);
                appBarLayout.collapseToolbar();
                collapsingToolbarLayout.setTitle("");
                break;
        }
    }

    @Override
    public void editProduct(Inventory inventory, boolean edit) {
        changeFragment(CreateProductFragment.newInstance(inventory, edit), true);
    }

    @Override
    public void scanCompleted(String parent, String barcode) {

        if(parent == InventoryFragment.class.getName()){
            Inventory item = Inventory.get(barcode);

            if(item == null){
                item = new Inventory();
                Product p = new Product();
                p.setUpc(barcode);

                item.setProduct(p);
            }else{
                if(item.getProduct().hasImages()) item.getProduct().loadImages();
            }

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            }

            editProduct(item, false);

        }

        if(parent == CreateProductFragment.class.getName()){
            currentFragment = getCreateProductFragment();
            getSupportFragmentManager().popBackStack();

            ((CreateProductFragment)currentFragment).setUPC(barcode);
        }
    }

    private Fragment getCreateProductFragment(){
        FragmentManager fragManager = this.getSupportFragmentManager();
        int count = this.getSupportFragmentManager().getBackStackEntryCount();

        Fragment fr = null;
        for(Fragment f : fragManager.getFragments()){
            if(f instanceof CreateProductFragment)
                fr = f;
        }
        return fr;
    }

}
