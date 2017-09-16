package app.nutrimeat.meat.org.nutrimeat.Checkout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.BuildConfig;
import app.nutrimeat.meat.org.nutrimeat.CommonFunctions;
import app.nutrimeat.meat.org.nutrimeat.Home.TrackGPS;
import app.nutrimeat.meat.org.nutrimeat.PrefManager;
import app.nutrimeat.meat.org.nutrimeat.R;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView_mbold;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView_regular;
import app.nutrimeat.meat.org.nutrimeat.api.API;
import app.nutrimeat.meat.org.nutrimeat.api.CheckUser;
import app.nutrimeat.meat.org.nutrimeat.api.UpdateOrderStatus;
import app.nutrimeat.meat.org.nutrimeat.payment.PayUActivity;
import app.nutrimeat.meat.org.nutrimeat.payment.PaymentActivity;
import app.nutrimeat.meat.org.nutrimeat.product.ModelCart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PREORDER_CART;
import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PRODUCT_CART;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener ,LocationListener{


    public static final int MAX_ORDER_DISTANCE = 6000;

    private static final String TAG = CheckoutActivity.class.getSimpleName();
    // ArrayList<ModelCart> ListofProdcuts = new ArrayList<>();
    private static final int MY_PERMISSIONS_REQUEST_LOCATION =01;

    RecyclerView Checkout_rv;

    ArrayList<Double> price = new ArrayList<>();
    //public double price = 0;
    p_MyCustomTextView_regular subtotal;
    private Button btnCheckout;
    private CheckoutAdapter checkoutAdapter;
    private Button mTextViewDate;
    private Button mTextViewTime;

    private LinearLayout mLinearLayoutDate;
    private Calendar mCalendar;

    private int hour, minute, month, year, day;
    private String txnId = "";
    private String amount = "";
    private String f_Name;
    private String email = "";
    private String phone = "";
    private String user_credentials = "";
    private String product_info = "";
    private ProgressDialog progressDialog;
    private String orderNum;
    private List<ModelCart> cart_itens, updatedCartItems = new ArrayList<>();
    private String deliveryLocation;
    private String landmark;
    private String orderType;
    private String transactionId;
    private  Location location;
    private PrefManager prefs;
    private Location deviceLocation ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendar = Calendar.getInstance();
        setContentView(R.layout.activity_checkout);
        prefs = new PrefManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        subtotal = (p_MyCustomTextView_regular) findViewById(R.id.subtotal);

        mTextViewDate = (Button) findViewById(R.id.textview_date);
        mTextViewTime = (Button) findViewById(R.id.textview_time);
        mLinearLayoutDate = (LinearLayout) findViewById(R.id.linear_date);
        mTextViewDate.setOnClickListener(this);
        mTextViewTime.setOnClickListener(this);

        p_MyCustomTextView_mbold empty_view = (p_MyCustomTextView_mbold) findViewById(R.id.empty_view);
        Checkout_rv = (RecyclerView) findViewById(R.id.Checkout_rv);
        btnCheckout = (Button) findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(this);
        cart_itens = CommonFunctions.getSharedPreferenceProductList(CheckoutActivity.this, PREF_PRODUCT_CART);

        if (cart_itens.size() == 0) {
            cart_itens.addAll(CommonFunctions.getSharedPreferenceProductList(CheckoutActivity.this, PREF_PREORDER_CART));
        }

        if (isPreorder()) {
            mTextViewDate.setText(getDate(mCalendar));
            mTextViewTime.setText(getTime(mCalendar));
        } else {
            mLinearLayoutDate.setVisibility(View.GONE);
        }

        if (cart_itens != null) {
            ArrayList<ModelCart> listOfStrings = new ArrayList<>(cart_itens.size());
            listOfStrings.addAll(cart_itens);
            //ListofProdcuts = (ArrayList<ModelCart>) getIntent().getBundleExtra("bundle").getSerializable("isadd_to_cart");
            Checkout_rv.setLayoutManager(new LinearLayoutManager(this));
            if (listOfStrings.size() > 0) {
                empty_view.setVisibility(View.GONE);
                Checkout_rv.setVisibility(View.VISIBLE);
                checkoutAdapter = new CheckoutAdapter(listOfStrings, R.layout.checkout_item, CheckoutActivity.this);
                Checkout_rv.setAdapter(checkoutAdapter);
            } else {
                empty_view.setVisibility(View.VISIBLE);
                Checkout_rv.setVisibility(View.GONE);
            }
        } else {
            empty_view.setVisibility(View.VISIBLE);
            Checkout_rv.setVisibility(View.GONE);
        }

    }

    private boolean isPreorder() {
        List<ModelCart> sharedPreferenceProductList = CommonFunctions.getSharedPreferenceProductList(this, PREF_PREORDER_CART);
        if (sharedPreferenceProductList.size() > 0) {
            return true;
        }

        return false;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("product", "product");
        intent.putExtra("position", "two");
        setResult(Activity.RESULT_OK, intent);
        finish();
        //  super.onBackPressed();
    }


    public boolean canCheckOut() {
        LocationManager mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        TrackGPS mLocListener = new TrackGPS();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION ,Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            Toast.makeText(this, "Please Enable Location to check our delivery availability", Toast.LENGTH_SHORT).show();
        }
        else {

            mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);


                location = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) == null ?
                        mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) : mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            }
            if( location != null )
            Toast.makeText(CheckoutActivity.this, "Latitude : "+location.getLatitude()+ " Longitude :" +
                    " "+location.getLongitude()+"", Toast.LENGTH_SHORT).show();
             // location is null , unable to fetch
            // the location details so maintaining it as null .
            return deviceLocation == null ? false : inRange( deviceLocation);
        }

        return false ;

    }

    public int getDistanceBetweenLatLang(Location loc1, Location loc2) {
        return (int) loc1.distanceTo(loc2);
    }

    public Location getCenterLocation() {
        Location location = new Location("");
        //Nutrimeat Location  17.3478735,78.5412692
        location.setLatitude(17.3478735);
        location.setLongitude(78.5412692);
        return location;
    }

    public boolean inRange(Location deviceLocation) {
        Toast.makeText(CheckoutActivity.this, "Your distance is about "+(getDistanceBetweenLatLang(getCenterLocation(),deviceLocation))/1000 +"kilometers from the Store .", Toast.LENGTH_SHORT).show();
        if(getDistanceBetweenLatLang(getCenterLocation(),deviceLocation) < MAX_ORDER_DISTANCE) {
            return true ;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheckout:
                PrefManager manager = new PrefManager(getApplicationContext());
                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                    if (canCheckOut()) {

                        List<ModelCart> cart_itens;
                        if (isPreorder()) {
                            cart_itens = CommonFunctions.getSharedPreferenceProductList(CheckoutActivity.this, PREF_PREORDER_CART);
                        } else {
                            cart_itens = CommonFunctions.getSharedPreferenceProductList(CheckoutActivity.this, PREF_PRODUCT_CART);
                        }

                        if (cart_itens != null && cart_itens.size() > 0) {
                            if (isPreorder()) {
                                if (mCalendar.get(Calendar.HOUR_OF_DAY) >= 19 || mCalendar.get(Calendar.HOUR_OF_DAY) < 8) {
                                    showPreOrderDiialog("Sorry! the items cannot be delivered in between 7PM to 8AM. Please update the time");
                                    return;
                                }
                                long diff = mCalendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
                                long day = 24 * 60 * 60 * 1000;
                                if (diff > day) {
//                                navigateToPayU();
                                    showDialog();
                                } else {
                                    Toast.makeText(this, "Please select valid date", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                if (mCalendar.get(Calendar.HOUR_OF_DAY) >= 19 || mCalendar.get(Calendar.HOUR_OF_DAY) < 8) {
                                    showPreOrderDiialog("Store is closed now. You can Pre-Order");
                                    return;
                                }

                                showDialog();
                            }

//                        navigateUserToPayment(cart_itens);
                        } else {
                            Toast.makeText(getApplicationContext(), "No items added to card", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "We cannot deliver order to your location", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Enable your location to check if order can be delivered to your location .", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.textview_date:
                Calendar calendar=Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        (calendar.get(Calendar.DAY_OF_MONTH) + 1));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

                break;
            case R.id.textview_time:
                new TimePickerDialog(this, time, mCalendar
                        .get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(this)).show();

                break;
        }
    }

    private void showPreOrderDiialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setPositiveButton(android.R.string.ok, null).setCancelable(false)
                .show();
    }


    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            mTextViewDate.setText(getDate(mCalendar));
        }

    };
    private TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {

            mCalendar.set(Calendar.HOUR_OF_DAY, hour);
            mCalendar.set(Calendar.MINUTE, minute);
            mTextViewTime.setText(getTime(mCalendar));

        }
    };

//
//    private void navigateUserToPayment(List<ModelCart> cart_itens) {
//        PrefManager manager = new PrefManager(this);
//        String tnxId = "0nf7" + System.currentTimeMillis();
//        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new
//                PayUmoneySdkInitilizer.PaymentParam.Builder().
//                setMerchantId("m3pWGL")
//                .setKey("m3pWGL")
//                .setIsDebug(true)                      // for Live mode -
//                .setIsDebug(false)
//                .setAmount(checkoutAdapter.getSub_total())
//                .setTnxId(tnxId)
//                .setPhone("8882434664")
//                .setProductName("product_name")
//                .setFirstName("piyush")
//                .setEmail("test@payu.in")
//                .setsUrl("http://www.nutrimeat.in/store/checkout_complete")
//                .setfUrl("http://www.nutrimeat.in/store/checkout_complete")
//                .setUdf1("")
//                .setUdf2("")
//                .setUdf3("")
//                .setUdf4("")
//                .setUdf5("");
//
//        // declare paymentParam object
//        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();
//        HashMap<String, String> params = new HashMap<>();
//        params.put("key", "m3pWGL");
//        params.put("txnid", tnxId);
//        params.put("amount", String.valueOf(checkoutAdapter.getSub_total()));
//        params.put("productinfo", "Nutri Mean");
//        params.put("firstname", manager.getName());
//        params.put("email", manager.getEmail());
//        String hash = Utils.generateHash(params, "RbHCQkbb");
////set the hash
//
//
//        String serverCalculatedHash = hashCal("SHA-512", hash);
//        paymentParam.setMerchantHash(hash);
//
//        // Invoke the following function to open the checkout page.
//        PayUmoneySdkInitilizer.startPaymentActivityForResult(CheckoutActivity.this, paymentParam);
//
//    }
//
//    public static String hashCal(String type, String str) {
//        byte[] hashseq = str.getBytes();
//        StringBuffer hexString = new StringBuffer();
//        try {
//            MessageDigest algorithm = MessageDigest.getInstance(type);
//            algorithm.reset();
//            algorithm.update(hashseq);
//            byte messageDigest[] = algorithm.digest();
//            for (int i = 0; i < messageDigest.length; i++) {
//                String hex = Integer.toHexString(0xFF & messageDigest[i]);
//                if (hex.length() == 1) {
//                    hexString.append("0");
//                }
//                hexString.append(hex);
//            }
//        } catch (NoSuchAlgorithmException nsae) {
//        }
//        return hexString.toString();
//    }
//
//    protected void os

    private void navigateToPayment() {
        Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
        intent.putExtra("amount", String.valueOf(checkoutAdapter.getSub_total()));
        startActivity(intent);
    }

    private void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.payment_type_alert, null);
        final EditText etLandmark = (EditText) dialogView.findViewById(R.id.etLandmark);
        final EditText etDeliveryLocation = (EditText) dialogView.findViewById(R.id.etDeliveryLocation);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.findViewById(R.id.textview_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deliveryLocationStr = etDeliveryLocation.getText().toString().trim();
                String landmarkStr = etDeliveryLocation.getText().toString().trim();
                if (TextUtils.isEmpty(deliveryLocationStr)) {
                    etDeliveryLocation.setError("Delivery location can't be empty");
                    etDeliveryLocation.requestFocus();
                } else if (TextUtils.isEmpty(landmarkStr)) {
                    etLandmark.setError("Landmark can't be empty");
                    etLandmark.requestFocus();
                }else{
                    deliveryLocation = etDeliveryLocation.getText().toString().trim();
                    landmark = etDeliveryLocation.getText().toString().trim();
                    orderType = "Cash On Delivery";
                    dialog.dismiss();
                    updateOrderStatus();

                }
            }
        });
        dialog.findViewById(R.id.textview_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderType = "Online Payment";
                dialog.dismiss();
                navigateToPayU();
            }
        });
        dialog.show();

    }

    private void updateOrderStatus() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        showProgressDialog(true);
        API api = retrofit.create(API.class);
        Call<GenerateOrderNoResponse> orderResponseCall = api.generateOrderNo();
//                                    progressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Validating Password...", true, false);
        orderResponseCall.enqueue(new Callback<GenerateOrderNoResponse>() {
            @Override
            public void onResponse(Call<GenerateOrderNoResponse> call, Response<GenerateOrderNoResponse> response) {
                showProgressDialog(false);
                if (response != null && response.body() != null) {
                    orderNum = response.body().getOrderno();
                    updatedCartItems.clear();
                    updatedCartItems.addAll(cart_itens);
                    addItemsToOrder();
                } else {
                    showErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<GenerateOrderNoResponse> call, Throwable t) {
                showErrorDialog();
                if (t != null) {
                    t.printStackTrace();
                }
            }
        });
    }

    private void addItemsToOrder() {
        if (updatedCartItems != null && !updatedCartItems.isEmpty()) {
            ModelCart modelCart = updatedCartItems.get(0);
            updatedCartItems.remove(0);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.api_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            showProgressDialog(true);
            API api = retrofit.create(API.class);
            CheckUser checkUser = new CheckUser();
            Call<AddOrderItemResponse> addOrderItemCall = api.addOrderItem(orderNum, String.valueOf(modelCart.getItem_id()), modelCart
                            .getItem_name(),
                    modelCart.getDesired_cut(), String.valueOf(modelCart.getItem_price()), modelCart.getQuantity(), modelCart.getItem_desc());
//                                    progressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Validating Password...", true, false);
            addOrderItemCall.enqueue(new Callback<AddOrderItemResponse>() {
                @Override
                public void onResponse(Call<AddOrderItemResponse> call, Response<AddOrderItemResponse> response) {
                    showProgressDialog(false);
                    addItemsToOrder();
                }

                @Override
                public void onFailure(Call<AddOrderItemResponse> call, Throwable t) {
                    showProgressDialog(false);
                    showErrorDialog();
                }
            });
        } else {
            addToCart();
        }
    }

    private void addToCart() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        showProgressDialog(true);
        final API api = retrofit.create(API.class);
        CheckUser checkUser = new CheckUser();
        PrefManager prefManager = new PrefManager(this);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String orderTime = dateFormat.format(new Date());
        Call<AddOrderItemResponse> addOrderItemCall = api.addOrder(orderNum  ,prefManager.getEmail(), 300,
                "", 0, checkoutAdapter.getSub_total(), cart_itens.size(), 1, 1, orderTime, prefManager.getName()==null?prefManager.getEmail():prefManager.getName(), deliveryLocation +","+landmark,
                "Hyderabad", "Telangana", "India", "500047",
                "LBNAGAR", prefManager.getMobile()==null?"9502675775":prefManager.getMobile() ,
                orderType,"standard", getDate(mCalendar),
                getTime(mCalendar), "" ,location == null ? "" :location.getLongitude()+"",location == null ? "" :location.getLatitude()+"");
//                                    progressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Validating Password...", true, false);
        addOrderItemCall.enqueue(new Callback<AddOrderItemResponse>() {
            @Override
            public void onResponse(Call<AddOrderItemResponse> call, Response<AddOrderItemResponse> response) {
                showProgressDialog(false);

                if(orderType.equalsIgnoreCase("Online Payment")) {
                    UpdateOrderStatus model = new UpdateOrderStatus();
                    model.setOrderNumber(orderNum);
                    model.setReferenceNumber(transactionId);
                    Call<Object> updateOrder = api.getOrderStatus(model);

                    updateOrder.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });

                }
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
                builder.setMessage("Your request has been placed").setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CommonFunctions.setSharedPreferenceProductList(CheckoutActivity.this, PREF_PRODUCT_CART,new ArrayList<ModelCart>());
                        CommonFunctions.setSharedPreferenceProductList(CheckoutActivity.this, PREF_PREORDER_CART,new ArrayList<ModelCart>());
                        finish();
                    }
                }).show();

            }

            @Override
            public void onFailure(Call<AddOrderItemResponse> call, Throwable t) {
                showProgressDialog(false);
                showErrorDialog();
            }
        });
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
        builder.setMessage("Unable to connect server. Please try again later").setCancelable(false).setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    private void showProgressDialog(boolean canDShowDialog) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(CheckoutActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Loading . . . ");
        }
        if (!canDShowDialog) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } else {
            progressDialog.show();
        }
    }

    private String getDate(Calendar calendar) {
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setTimeZone(calendar.getTimeZone());
        String date = dateFormat.format(calendar.getTime());
        return "Oder Date : " + date;
    }

    private String getTime(Calendar calendar) {
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(calendar.getTimeZone());
        String time = timeFormat.format(calendar.getTime());
        return "Time : " + time;
    }


    /******************************************************/
    private void navigateToPayU() {

        txnId = "NM_" + System.currentTimeMillis();
        amount = String.valueOf(checkoutAdapter.getSub_total());
        PrefManager prefManager = new PrefManager(this);

        f_Name = prefManager.getName();
        email = prefManager.getEmail();
        phone = prefManager.getMobile();

        if (TextUtils.isEmpty(email))
            email = "";

        if (TextUtils.isEmpty(phone))
            phone = "";

        product_info = "Nutrimate";
        user_credentials = BuildConfig.MERCHANT_KEY + ":" + email;
        String postData = "&txnid=" + txnId +
                "&device_type=1" +
                "&ismobileview=1" +
                "&productinfo=" + product_info +
                "&user_credentials=" + user_credentials +
                "&key=" + BuildConfig.MERCHANT_KEY +
                "&instrument_type=Put here Device info " +
                "&surl=" + BuildConfig.S_URL +
                "&furl=" + BuildConfig.F_URL +
                "&instrument_id=7dd17561243c202" +
                "&firstname=" + f_Name +
                "&email=" + email +
                "&phone=" + phone +
                "&amount=" + amount +
//                "&bankcode=PAYUW" + //for PayU Money
//                "&pg=WALLET"+//for PayU Money
                "&hash=";
//        generateHashFromServer();

        MessageDigest digest = null;
        String hash;
        StringBuilder checkSumStr = new StringBuilder();
        try {
            digest = MessageDigest.getInstance("SHA-512");// MessageDigest.getInstance("SHA-256");
            checkSumStr.append(BuildConfig.MERCHANT_KEY);
            checkSumStr.append("|");
            checkSumStr.append(txnId);
            checkSumStr.append("|");
            checkSumStr.append(amount);
            checkSumStr.append("|");
            checkSumStr.append(product_info);
            checkSumStr.append("|");
            checkSumStr.append(f_Name);
            checkSumStr.append("|");
            checkSumStr.append(email);
            checkSumStr.append("|||||||||||");
            checkSumStr.append(BuildConfig.SALT_KEY);

            digest.update(checkSumStr.toString().getBytes());

            hash = bytesToHexString(digest.digest());
            postData += hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, PayUActivity.class);
        intent.putExtra("postData", postData);
        intent.putExtra("txn", txnId);
        startActivityForResult(intent, 100);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                //update order status api
                transactionId= data.getStringExtra(getString(R.string.cb_payu_response));
                updateOrderStatus();
            } else {
                showErrorDialog();
                //todo call cancel order api
            }
        }
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');

            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /******************************************************/


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION :
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Location Tag", "Location access give");
                    // permissagion was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        deviceLocation = location;
    }
}
