package app.nutrimeat.meat.org.nutrimeat.WatsCooking;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.ApiClient;
import app.nutrimeat.meat.org.nutrimeat.Checkout.CheckoutActivity;
import app.nutrimeat.meat.org.nutrimeat.CommonFunctions;
import app.nutrimeat.meat.org.nutrimeat.Navdrawer;
import app.nutrimeat.meat.org.nutrimeat.ProductDetails.Products_Details_Model;
import app.nutrimeat.meat.org.nutrimeat.R;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView_mbold;
import app.nutrimeat.meat.org.nutrimeat.api.API;
import app.nutrimeat.meat.org.nutrimeat.product.ModelCart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PREORDER_CART;
import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PRODUCT_CART;



public class WatsCookingFragment extends Fragment {

    private TextView txtViewCount;
    private ProgressBar progressBar;
    ModelCart buynow;
    WatsCooking_Model watsCooking_model;
    int click_position, serving_position;

    public WatsCookingFragment() {
        // Required empty public constructor
    }

    WatsCookingAdapter adapter;

    public static WatsCookingFragment newInstance(String cat) {
        WatsCookingFragment fragment = new WatsCookingFragment();
        Bundle args = new Bundle();
        args.putString("cat", cat);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View rootview = inflater.inflate(R.layout.fragment_menu_products_specific, container, false);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar);
//        setHasOptionsMenu(true);
        final p_MyCustomTextView_mbold emptyview = (p_MyCustomTextView_mbold) rootview.findViewById(R.id.emptyview);
        final RecyclerView mRecycler = (RecyclerView) rootview.findViewById(R.id.products_rv);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        // mRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        progressBar.setVisibility(View.VISIBLE);
        API apiService =
                ApiClient.getClient().create(API.class);
        Call<List<WatsCooking_Model>> call = apiService.watscooking();

        call.enqueue(new Callback<List<WatsCooking_Model>>() {
            @Override
            public void onResponse(Call<List<WatsCooking_Model>> call, Response<List<WatsCooking_Model>> response) {
                List<WatsCooking_Model> products = response.body();
                if (response.body() == null || products.size() == 0) {
                    emptyview.setVisibility(View.VISIBLE);
                    mRecycler.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    emptyview.setVisibility(View.GONE);
                    mRecycler.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    adapter = new WatsCookingAdapter(products, R.layout.watscooking_item, WatsCookingFragment.this);
                    mRecycler.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<WatsCooking_Model>> call, Throwable t) {
                // Log error here since request failed
                Log.e("Products", t.toString());
            }
        });

        return rootview;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Products");
    }

    public void add_product(List<WatsCooking_Model> products, int position, int serv_position) {
        watsCooking_model = products.get(position);
        click_position = position;
        serving_position = serv_position;
        get_product_details();
    }

    private void get_product_details() {
        API apiService = ApiClient.getClient().create(API.class);
        Call<Products_Details_Model> call = apiService.products_details(String.valueOf(watsCooking_model.getItem_id()));
        new NetworkCall().execute(call);

    }

    private class NetworkCall extends AsyncTask<Call, Void, Products_Details_Model> {
        @Override
        protected Products_Details_Model doInBackground(Call... params) {
            try {
                Call<Products_Details_Model> call = params[0];
                Response<Products_Details_Model> response = call.execute();
                Products_Details_Model model = response.body();
                return model;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Products_Details_Model Products_Details) {
//            Products_Details_Model Products_Details = response.body();
            Log.e("got response", "hi");
            final List<String> quantityList = Arrays.asList(Products_Details.getQuantity().split(","));
            final List<String> desiredList = Arrays.asList(Products_Details.getDesired_cut().split(","));
            String quantity = null;
            if (serving_position == 0) {
                quantity = String.valueOf(watsCooking_model.getPeople2());
            } else if (serving_position == 1) {
                quantity = String.valueOf(watsCooking_model.getPeople4());

            } else if (serving_position == 2) {
                quantity = String.valueOf(watsCooking_model.getPeople6());

            }
            Log.e("got data", "true");

            int quantity_selected = quantityList.indexOf(quantity);
            int desired_selected = desiredList.indexOf(watsCooking_model.getCut());
            ModelCart get_model = getmodel(Products_Details, desired_selected, quantity_selected);
            if (get_model != null) {
                updateshared(get_model);
                Intent intent = new Intent(getActivity(), Navdrawer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ids", R.id.home);
                getActivity().startActivityForResult(intent, 1);
                Toast.makeText(getActivity(),"Product added to cart",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private ModelCart getmodel(Products_Details_Model product, int desired_position, int quantity_position) {
        ModelCart modelcart = new ModelCart();
        modelcart.setItem_id(product.getItem_id());
        modelcart.setItem_cat(product.getItem_cat());
        modelcart.setItem_image(product.getItem_image());
        modelcart.setItem_name(product.getItem_name());
        modelcart.setItem_price(product.getItem_price());
        modelcart.setItem_desc(product.getItem_desc());
        modelcart.setUnit(product.getUnit());
        modelcart.setDesired_cut(product.getDesired_cut());
        modelcart.setQuantity(product.getQuantity());
        modelcart.setSelected_desired_cut(String.valueOf(desired_position));
        modelcart.setSelected_quantity(String.valueOf(quantity_position));
        return modelcart;

    }

    private void updateshared(ModelCart modelCart_pojo) {
        List<ModelCart> set_isadd_to_cart = CommonFunctions.getSharedPreferenceProductList(getActivity(), PREF_PRODUCT_CART);
        if(set_isadd_to_cart==null) {
          set_isadd_to_cart=new ArrayList<>();
        }
            int get_pos = getpositonincart(set_isadd_to_cart, modelCart_pojo);
            if (get_pos != 0) {
                get_pos = get_pos - 1;
                set_isadd_to_cart.set(get_pos, modelCart_pojo);
            } else {
                set_isadd_to_cart.add(modelCart_pojo);
            }

        CommonFunctions.setSharedPreferenceProductList(getActivity(), PREF_PRODUCT_CART, set_isadd_to_cart);
    }

    private int getpositonincart(List<ModelCart> isadd_to_cart, ModelCart modelCart_pojo) {
        if (isadd_to_cart != null)
        {
            for (int i = 1; i < isadd_to_cart.size() + 1; i++)
            {

                if (isadd_to_cart.get(i - 1).getItem_id() == modelCart_pojo.getItem_id()) {
                    return i;
                }
            }
        } else {
            return 0;
        }
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Navdrawer) getActivity()).updateHotCount();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        final View notificaitons = menu.findItem(R.id.action_cart).getActionView();

        txtViewCount = (TextView) notificaitons.findViewById(R.id.txtCount);
        // update menu
        final List<ModelCart> isadd_to_cart = CommonFunctions.getSharedPreferenceProductList(getActivity(), PREF_PRODUCT_CART);
        if (isadd_to_cart.size()==0) {

            isadd_to_cart.addAll(CommonFunctions.getSharedPreferenceProductList(getActivity(), PREF_PREORDER_CART));
        }

        updateHotCount(isadd_to_cart);


        notificaitons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                getActivity().startActivity(intent);
            }
        });
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("I'm Back", "first");
        if (requestCode == 1) {
            Log.e("I'm Back", "secound");
            if (resultCode == Activity.RESULT_OK) {
                List<ModelCart> isadd_to_cart = CommonFunctions.getSharedPreferenceProductList(getActivity(), PREF_PRODUCT_CART);
//                updateHotCount(isadd_to_cart);
                ((Navdrawer) getActivity()).updateHotCount();
                adapter.notifyDataSetChanged();
                Log.e("I'm Back", "third");
            }
        }
    }


}