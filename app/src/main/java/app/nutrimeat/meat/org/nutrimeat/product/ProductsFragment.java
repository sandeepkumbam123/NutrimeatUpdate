package app.nutrimeat.meat.org.nutrimeat.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import app.nutrimeat.meat.org.nutrimeat.R;


public class ProductsFragment extends Fragment {

    private TextView txtViewCount;
    private ProgressBar progressBar;

    public ProductsFragment() {
        // Required empty public constructor
    }


    public static ProductsFragment newInstance() {
        ProductsFragment fragment = new ProductsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View rootview = inflater.inflate(R.layout.fragment_menu_products, container, false);
//        setHasOptionsMenu(true);
        ViewPager viewPager = (ViewPager) rootview.findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) rootview.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new ProductsPagerAdapter(getChildFragmentManager()));
        return rootview;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Products");
    }
/*
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
    }*/
}