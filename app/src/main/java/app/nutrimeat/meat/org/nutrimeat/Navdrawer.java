package app.nutrimeat.meat.org.nutrimeat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.Account.AcountFragment;
import app.nutrimeat.meat.org.nutrimeat.Checkout.CheckoutActivity;
import app.nutrimeat.meat.org.nutrimeat.Home.StatsResponseModel;
import app.nutrimeat.meat.org.nutrimeat.Home.TrackGPS;
import app.nutrimeat.meat.org.nutrimeat.drawer.BulkOrder;
import app.nutrimeat.meat.org.nutrimeat.drawer.ContactUs;
import app.nutrimeat.meat.org.nutrimeat.drawer.Recipes;
import app.nutrimeat.meat.org.nutrimeat.product.ModelCart;
import app.nutrimeat.meat.org.nutrimeat.product.Products;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PREORDER_CART;
import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PRODUCT_CART;

public class Navdrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    private BottomSheetBehavior<View> mBottomSheetBehavior;
    int ids;
    private PrefManager prefManager;
    private StatsResponseModel statsResponseModel = null;
    private TextView txtViewCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navdrawer);
        prefManager = new PrefManager(this);
        startService(new Intent(getApplicationContext(), TrackGPS.class));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.defaultColor));
        setSupportActionBar(toolbar);
        ids = getIntent().getIntExtra("ids", R.id.home);
        DrawerLayout drawerLayout = (DrawerLayout)
                findViewById(R.id.drawer_layout);

        drawerLayout.setStatusBarBackground(R.color.defaultColor);
        // View bottomSheet = findViewById( R.id.bottom_sheet1 );
        //mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        //mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        View header = navigationView.getHeaderView(0);
        TextView emailNav = (TextView) header.findViewById(R.id.emailNav);
        if (prefManager.getName() != null) {
            emailNav.setText(prefManager.getName());
        }
        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(ids);



    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        getSupportActionBar().setTitle(title);
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            if (fragment instanceof Recipes) {
                setTitle("Home");
            } else if (fragment instanceof AcountFragment) {
                setTitle("Account");
            } else if (fragment instanceof ContactUs) {
                setTitle("Contact Us");
            } else if (fragment instanceof BulkOrder) {
                setTitle("Bulk Order");
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public StatsResponseModel getStatsResponseModel() {
        return statsResponseModel;
    }

    public void setStatsResponseModel(StatsResponseModel statsResponseModel) {
        this.statsResponseModel = statsResponseModel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        final View notificaitons = menu.findItem(R.id.action_cart).getActionView();

        txtViewCount = (TextView) notificaitons.findViewById(R.id.txtCount);

        updateHotCount();

        notificaitons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Navdrawer.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });

        return true;
    }

    public void updateHotCount() {
        // update menu
        final List<ModelCart> isadd_to_cart = CommonFunctions.getSharedPreferenceProductList(this, PREF_PRODUCT_CART);
        if (isadd_to_cart != null && isadd_to_cart.size() == 0) {
            List<ModelCart> list = CommonFunctions.getSharedPreferenceProductList(this, PREF_PREORDER_CART);
            isadd_to_cart.addAll(list);
        }
        if (isadd_to_cart != null) {
            final int count = isadd_to_cart.size();
            if (count == 0)
                txtViewCount.setVisibility(View.GONE);
            else {
                txtViewCount.setVisibility(View.VISIBLE);
                txtViewCount.setText(Integer.toString(count));
            }
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            show_fliter_dailog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

    public void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        String title = null;
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.home:
//                fragment = HomeFragment.newInstance("All");
                fragment = new Recipes();
                title = "Home";
                break;
          /*  case R.id.products:
                fragment = ProductsFragment.newInstance();
                title = "Products";
                break;*/
           /* case R.id.nav_wc:
                fragment = new WatsCookingFragment();
                title = "What's Cooking";
                break;
            case R.id.nav_recipes:
                fragment = new RecipiesFragment();
                title = "Recipies";
                break;*/
            case R.id.nav_youraccount:
                fragment = new AcountFragment();
                title = "Account";
                break;
            case R.id.nav_contactus:
                fragment = new ContactUs();
                title = "Contact Us";
                break;
            case R.id.nav_bulk:
                fragment = new BulkOrder();
                title = "Bulk Order";
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        getSupportActionBar().setTitle(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onRestart() {
        super.onRestart();
//        finish();
//        startActivity(getIntent());
    }

    public void show_fliter_dailog() {
        final CharSequence[] items = {"All", "chicken", "mutton", "sea foods"};
        // arraylist to keep the selected items
        final ArrayList seletedItems = new ArrayList();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select  Category")
                .setSingleChoiceItems(items, 0, null)
                .setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        Products fragment = Products.newInstance(items[selectedPosition].toString());
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                        // Do something useful withe the position of the selected radio button
                    }
                }).create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(getApplicationContext(), TrackGPS.class));
        super.onDestroy();
    }

    @Override
    public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            if (fragment instanceof Recipes) {
                setTitle("Home");
            } else if (fragment instanceof AcountFragment) {
                setTitle("Account");
            } else if (fragment instanceof ContactUs) {
                setTitle("Contact Us");
            } else if (fragment instanceof BulkOrder) {
                setTitle("Bulk Order");
            }
        }
    }
}

