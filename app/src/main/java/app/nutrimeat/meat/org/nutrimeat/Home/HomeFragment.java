package app.nutrimeat.meat.org.nutrimeat.Home;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.nutrimeat.meat.org.nutrimeat.ApiClient;
import app.nutrimeat.meat.org.nutrimeat.Checkout.CheckoutActivity;
import app.nutrimeat.meat.org.nutrimeat.CommonFunctions;
import app.nutrimeat.meat.org.nutrimeat.R;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView_mbold;
import app.nutrimeat.meat.org.nutrimeat.api.API;
import app.nutrimeat.meat.org.nutrimeat.api.CheckArea;
import app.nutrimeat.meat.org.nutrimeat.product.ModelCart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PRODUCT_CART;



public class HomeFragment extends Fragment {
    protected static final String TAG = "main-activity";

    //    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
//    protected static final String LOCATION_ADDRESS_KEY = "location-address";
//    private static final int MY_PERMISSION_LOCATION = 1;
    private p_MyCustomTextView_mbold mLocationAddressTextView;
//    private static final int RC_CAMERA_PERM = 123;
//    private static final int RC_LOCATION_CONTACTS_PERM = 124;
//    boolean location_permission = false;
//    private LocationManager locationManager;
//    private Criteria criteria;
//    private String bestProvider;
//    private double latitude;
//    private double longitude;

    private TrackGPS gps;
    double longitude;
    double latitude;
    private p_MyCustomTextView_mbold delivery_name;
    private TextView txtViewCount;


    /**
     * Provides the entry point to Google Play services.
     */


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String cat) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("cat", cat);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        delivery_name = (p_MyCustomTextView_mbold) rootview.findViewById(R.id.delivery_status);
        mLocationAddressTextView = (p_MyCustomTextView_mbold) rootview.findViewById(R.id.location_address_view);

        gps = new TrackGPS(getActivity());


        if (gps.canGetLocation()) {

            longitude = gps.getLongitude();
            latitude = gps.getLatitude();
            getAddress(latitude, longitude);
            //Toast.makeText(getActivity(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();
        } else {

            gps.showSettingsAlert();
        }
        // locationAndContactsTask();
        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Products");
    }

    public void updateHotCount(List<ModelCart> selc_products) {
        if (selc_products != null) {
            final int count = selc_products.size();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (count == 0)
                        txtViewCount.setVisibility(View.GONE);
                    else {
                        txtViewCount.setVisibility(View.VISIBLE);
                        txtViewCount.setText(Integer.toString(count));
                        // supportInvalidateOptionsMenu();
                    }
                }
            });
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        final View notificaitons = menu.findItem(R.id.action_cart).getActionView();

        txtViewCount = (TextView) notificaitons.findViewById(R.id.txtCount);
        // update menu
        final List<ModelCart> isadd_to_cart = CommonFunctions.getSharedPreferenceProductList(getActivity(), PREF_PRODUCT_CART);
        if (isadd_to_cart != null) {
            updateHotCount(isadd_to_cart);
        }

        notificaitons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                Address obj = addresses.get(0);
                String add = obj.getAddressLine(0);
                add = add + "\n" + obj.getCountryName();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubLocality();
                add = add + "\n" + obj.getLocality();
                add = add + "\n" + obj.getSubThoroughfare();
                mLocationAddressTextView.setText(obj.getSubLocality());
                Log.v("IGA", "Address" + add);
                API apiService =
                        ApiClient.getClient().create(API.class);
                CheckArea checkArea = new CheckArea();
                checkArea.setArea(obj.getSubLocality());
                Call<CheckAreaReponse> call = apiService.checkarea(checkArea);

                call.enqueue(new Callback<CheckAreaReponse>() {
                    @Override
                    public void onResponse(Call<CheckAreaReponse> call, Response<CheckAreaReponse> response) {
                        CheckAreaReponse products = response.body();
                        delivery_name.setText(products.getMessage());

                    }

                    @Override
                    public void onFailure(Call<CheckAreaReponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("Products", t.toString());
                    }
                });

            }

//            else {
//                if(gps.canGetLocation()){
//
//
//                    longitude = gps.getLongitude();
//                    latitude = gps .getLatitude();
//                    getAddress(latitude,longitude);
//                    Toast.makeText(getActivity(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//
//                    gps.showSettingsAlert();
//                }
//            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getActivity(), "Check your network", Toast.LENGTH_SHORT).show();
        }
    }
//
//    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
//    public void locationAndContactsTask() {
//        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
//            // Have permissions, do the thing!
//            final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            final double longitude = location.getLongitude();
//            final double latitude = location.getLatitude();
//            getAddress(latitude, longitude);
//        } else {
//            // Ask for both permissions
//            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location_contacts),
//                    RC_LOCATION_CONTACTS_PERM, perms);
//        }
//    }
//
//    public static boolean isLocationEnabled(Context context) {
//        int locationMode = 0;
//        String locationProviders;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            try {
//                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
//            } catch (Settings.SettingNotFoundException e) {
//                e.printStackTrace();
//            }
//            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
//        } else {
//            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//            return !TextUtils.isEmpty(locationProviders);
//        }
//    }
//
//
//    protected void getLocation() {
//        if (isLocationEnabled(getActivity())) {
//            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//            criteria = new Criteria();
//            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
//
//            //You can still do this if you like, you might get lucky:
//            Location location = locationManager.getLastKnownLocation(bestProvider);
//            if (location != null) {
//                Log.e("TAG", "GPS is on");
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//                Toast.makeText(getActivity(), "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
//
//            } else {
//                //This is what you need:
//                locationManager.requestLocationUpdates(bestProvider, 1000, 0, (android.location.LocationListener) getActivity());
//            }
//        } else {
//            //prompt user to enable location....
//            //.................
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//        //remove location callback:
//        locationManager.removeUpdates((android.location.LocationListener) this);
//
//        //open the map:
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//        Toast.makeText(getActivity(), "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
//
//    }
////    final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
////    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
////    final double longitude = location.getLongitude();
////    final double latitude = location.getLatitude();
////    getAddress(latitude, longitude);

}