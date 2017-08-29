package app.nutrimeat.meat.org.nutrimeat.Recipies;

import android.app.Activity;
import android.content.Intent;
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

import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.ApiClient;
import app.nutrimeat.meat.org.nutrimeat.Checkout.CheckoutActivity;
import app.nutrimeat.meat.org.nutrimeat.CommonFunctions;
import app.nutrimeat.meat.org.nutrimeat.Navdrawer;
import app.nutrimeat.meat.org.nutrimeat.R;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView_mbold;
import app.nutrimeat.meat.org.nutrimeat.api.API;
import app.nutrimeat.meat.org.nutrimeat.product.ModelCart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PRODUCT_CART;


public class RecipiesFragment extends Fragment {

    private TextView txtViewCount;
    private ProgressBar progressBar;

    public RecipiesFragment() {
        // Required empty public constructor
    }

    RecipiesAdapter adapter;

    public static RecipiesFragment newInstance(String cat) {
        RecipiesFragment fragment = new RecipiesFragment();
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
        setHasOptionsMenu(false);
        final p_MyCustomTextView_mbold emptyview = (p_MyCustomTextView_mbold) rootview.findViewById(R.id.emptyview);
        final RecyclerView mRecycler = (RecyclerView) rootview.findViewById(R.id.products_rv);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        // mRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        progressBar.setVisibility(View.VISIBLE);
        API apiService =
                ApiClient.getClient().create(API.class);
        Call<List<Recipies_Model>> call = apiService.recipies();

        call.enqueue(new Callback<List<Recipies_Model>>() {
            @Override
            public void onResponse(Call<List<Recipies_Model>> call, Response<List<Recipies_Model>> response) {
                List<Recipies_Model> products = response.body();
                if (response.body() == null || products.size() == 0) {
                    emptyview.setVisibility(View.VISIBLE);
                    mRecycler.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    emptyview.setVisibility(View.GONE);
                    mRecycler.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

//                    ArrayList<String> isadd_to_cart = new ArrayList<>();
//                    for (int i = 0; i < products.size(); i++)
//                        isadd_to_cart.add("true");
                    //    CommonFunctions.setSharedPreferenceStringList(getContext(), "isadd_to_cart", isadd_to_cart);
                    adapter = new RecipiesAdapter(products, R.layout.recipies_item, getActivity());
                    mRecycler.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<List<Recipies_Model>> call, Throwable t) {
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

    @Override
    public void onResume() {
        super.onResume();
        ((Navdrawer) getActivity()).updateHotCount();
    }
}