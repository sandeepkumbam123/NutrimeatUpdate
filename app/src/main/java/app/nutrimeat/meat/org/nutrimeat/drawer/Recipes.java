package app.nutrimeat.meat.org.nutrimeat.drawer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import app.nutrimeat.meat.org.nutrimeat.Home.Ads;
import app.nutrimeat.meat.org.nutrimeat.Home.StatsResponseModel;
import app.nutrimeat.meat.org.nutrimeat.Navdrawer;
import app.nutrimeat.meat.org.nutrimeat.R;
import app.nutrimeat.meat.org.nutrimeat.Recipies.RecipiesFragment;
import app.nutrimeat.meat.org.nutrimeat.WatsCooking.WatsCookingFragment;
import app.nutrimeat.meat.org.nutrimeat.api.API;
import app.nutrimeat.meat.org.nutrimeat.product.ProductsFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Recipes extends Fragment implements View.OnClickListener {

    private ProgressDialog progressDialog;
    private TextView tvDeliveryStatus, tvHappyCustomers, tvKilosSold;
    private Handler mHandler = new Handler();
    private static final long DELAY = 3000;
    private ViewPager viewpager;
    private ArrayList<Ads> mArrayListAds;
    private HomePagerAdapter mHomeAdsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, DELAY);
    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            int currentItem = viewpager.getCurrentItem();

            if (currentItem < viewpager.getChildCount() - 1) {
                currentItem += 1;
            } else if (currentItem == viewpager.getChildCount()) {
                currentItem = 0;
            }
            viewpager.setCurrentItem(currentItem);
            mHandler.postDelayed(mRunnable, DELAY);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.recipes_sample, container, false);
        viewpager = (ViewPager) view.findViewById(R.id.viewPager);
        mArrayListAds = new ArrayList<>();
        mHomeAdsAdapter=new HomePagerAdapter(getChildFragmentManager(),mArrayListAds);
        viewpager.setAdapter(mHomeAdsAdapter);

        view.findViewById(R.id.tvProducts).setOnClickListener(this);
        view.findViewById(R.id.tvRecipies).setOnClickListener(this);
        view.findViewById(R.id.tvWhatsCooking).setOnClickListener(this);
        tvKilosSold = (TextView) view.findViewById(R.id.tvKilosSold);
        tvHappyCustomers = (TextView) view.findViewById(R.id.tvHappyCustomers);
        tvDeliveryStatus = (TextView) view.findViewById(R.id.tvOrdersDelivered);
        if (((Navdrawer) getActivity()).getStatsResponseModel() == null) {
            reqestRecipeStatus();
        } else {
            StatsResponseModel responseModel = ((Navdrawer) getActivity()).getStatsResponseModel();
            tvDeliveryStatus.setText(responseModel.getDelivered());
            tvKilosSold.setText(responseModel.getSold());
            tvHappyCustomers.setText(responseModel.getHappycustomers());
        }

        getAds();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
        ((Navdrawer) getActivity()).setTitle("Home");
    }


    private void getAds() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);
        Call<String> responseCall = api.getAds();
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseStr = response.body();
                if (responseStr != null) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(responseStr);
                        Iterator<String> keys = object.keys();
                        for (; keys.hasNext(); ) {
                            try {
                                Ads ads = new Ads();
                                JSONObject ad = object.getJSONObject(keys.next());
                                ads.setImage(ad.getString("image"));
                                ads.setText(ad.getString("text"));
                                mArrayListAds.add(ads);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mHomeAdsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("RESPONSE_Login", "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Internal error. Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reqestRecipeStatus() {
        progressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Logging User...", true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);
        Call<StatsResponseModel> responseCall = api.getStats();
        responseCall.enqueue(new Callback<StatsResponseModel>() {
            @Override
            public void onResponse(Call<StatsResponseModel> call, Response<StatsResponseModel> response) {
                StatsResponseModel responseModel = response.body();
                progressDialog.dismiss();
                if (responseModel != null) {
                    if (getActivity() != null) {
                        ((Navdrawer) getActivity()).setStatsResponseModel(responseModel);
                    }
                    tvDeliveryStatus.setText(responseModel.getDelivered());
                    tvKilosSold.setText(responseModel.getSold());
                    tvHappyCustomers.setText(responseModel.getHappycustomers());
                }
            }

            @Override
            public void onFailure(Call<StatsResponseModel> call, Throwable t) {
                Log.d("RESPONSE_Login", "onFailure: " + t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Internal error. Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Recipes");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvProducts:
                //replacing the fragment
                replacceFragment(ProductsFragment.newInstance());
                ((Navdrawer) getActivity()).getSupportActionBar().setTitle("Products");
                break;
            case R.id.tvRecipies:
                //replacing the fragment
                replacceFragment(new RecipiesFragment());
                ((Navdrawer) getActivity()).getSupportActionBar().setTitle("Recipes");
                break;
            case R.id.tvWhatsCooking:
                //replacing the fragment
                replacceFragment(new WatsCookingFragment());
                ((Navdrawer) getActivity()).getSupportActionBar().setTitle("What's Cooking");
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void replacceFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        String backStateName = fragment.getClass().getName();
        ft.replace(R.id.content_frame, fragment).addToBackStack(backStateName);
        ft.commit();
    }
}