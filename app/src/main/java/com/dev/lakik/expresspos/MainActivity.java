package com.dev.lakik.expresspos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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

import com.dev.lakik.expresspos.Fragments.CreditsFragment;
import com.dev.lakik.expresspos.Fragments.LoginFragment;
import com.dev.lakik.expresspos.Fragments.POSPaymentFragment;
import com.dev.lakik.expresspos.Fragments.POSSummaryFragment;
import com.dev.lakik.expresspos.Fragments.RegisterFragment;
import com.dev.lakik.expresspos.Fragments.SettingsFragment;
import com.dev.lakik.expresspos.Fragments.SplashFragment;

import com.dev.lakik.expresspos.CustomView.ControllableAppBarLayout;
import com.dev.lakik.expresspos.Database.DBHelper;
import com.dev.lakik.expresspos.Fragments.CreateProductFragment;
import com.dev.lakik.expresspos.Fragments.FullScannerFragment;
import com.dev.lakik.expresspos.Fragments.InventoryFragment;
import com.dev.lakik.expresspos.Fragments.OrdersFragment;
import com.dev.lakik.expresspos.Fragments.POSFragment;
import com.dev.lakik.expresspos.Model.Company;
import com.dev.lakik.expresspos.Model.Const;
import com.dev.lakik.expresspos.Model.Inventory;
import com.dev.lakik.expresspos.Model.Product;
import com.dev.lakik.expresspos.Model.ProductImage;
import com.dev.lakik.expresspos.Model.Transaction;
import com.soundcloud.android.crop.Crop;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import java.util.ArrayList;
import java.util.UUID;

import static java.lang.System.in;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InventoryFragment.OnFragmentInteractionListener,
        CreateProductFragment.OnFragmentInteractionListener,
        POSFragment.OnFragmentInteractionListener,
        POSSummaryFragment.OnFragmentInteractionListener,
        POSPaymentFragment.OnFragmentInteractionListener,
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

        changeFragment(POSFragment.newInstance(), false);
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
                break;
            case R.id.nav_inventory:
                changeFragment(InventoryFragment.newInstance(), false);
                appBarLayout.collapseToolbar();
                break;
            case R.id.nav_orders:
                changeFragment(OrdersFragment.newInstance(), false);
                appBarLayout.collapseToolbar();
                break;
            case R.id.nav_credits:
                changeFragment(new CreditsFragment(), false);
                setToolbarTitle("Credits");
                appBarLayout.collapseToolbar();
                break;
            case R.id.nav_settings:
                changeFragment(new SettingsFragment(), false);
                setToolbarTitle("Settings");
                appBarLayout.collapseToolbar();
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
                finish();
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
            case Const.PAYMENT_FRAGMENT_FROM_POS:
                Transaction trans = ((POSFragment)currentFragment).getTransaction();
                changeFragment(POSPaymentFragment.newInstance(trans), false);
                appBarLayout.collapseToolbar();
                toolbar.setTitle("" + trans.getTotal());
                break;
            case Const.CREATE_NEW_PRODUCT_FRAGMENT:
                changeFragment(CreateProductFragment.newInstance(), true);
                appBarLayout.collapseToolbar();
                break;
            case Const.SCANNER_FRAGMENT_FROM_INVENTORY:
                changeFragment(FullScannerFragment.newInstance(InventoryFragment.class.getName()), true);
                appBarLayout.collapseToolbar();
                break;
            case Const.SCANNER_FRAGMENT_FROM_CREATE_PRODUCT:
                changeFragment(FullScannerFragment.newInstance(CreateProductFragment.class.getName()), true);
                appBarLayout.collapseToolbar();
                break;
            case Const.SCANNER_FRAGMENT_FROM_POS:
                changeFragment(FullScannerFragment.newInstance(POSFragment.class.getName()), true);
                appBarLayout.collapseToolbar();
                break;
            case Const.RELOAD_CREATE_PRODUCT:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }
                Inventory inv = ((CreateProductFragment)currentFragment).getInventory();
                boolean mode = ((CreateProductFragment)currentFragment).getEdit();
                changeFragment(CreateProductFragment.newInstance(inv, mode), true);
                appBarLayout.collapseToolbar();

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

            //((CreateProductFragment)currentFragment).setUPC(barcode);

            Inventory item = Inventory.get(barcode);

            if(item == null){

                item = ((CreateProductFragment)currentFragment).getInventory();
                item.getProduct().setUpc(barcode);

            }else{
                if(item.getProduct().hasImages()) item.getProduct().loadImages();
            }

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            }

            editProduct(item, false);
        }

        if(parent == POSFragment.class.getName()){
            currentFragment = getPOSFragment();
            getSupportFragmentManager().popBackStack();

            Inventory inv = Inventory.get(barcode);
            ((POSFragment)currentFragment).addProduct(inv.getProduct());
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

    private Fragment getPOSFragment(){
        FragmentManager fragManager = this.getSupportFragmentManager();
        int count = this.getSupportFragmentManager().getBackStackEntryCount();

        Fragment fr = null;
        for(Fragment f : fragManager.getFragments()){
            if(f instanceof POSFragment)
                fr = f;
        }
        return fr;
    }

    @Override
    public void showTransactionSummary(Transaction transaction) {
        changeFragment(POSSummaryFragment.newInstance(transaction), true);

    }

    @Override
    public void setToolbarTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
    }
}
