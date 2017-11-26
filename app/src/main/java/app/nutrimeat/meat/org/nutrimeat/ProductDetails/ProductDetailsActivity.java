package app.nutrimeat.meat.org.nutrimeat.ProductDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.ApiClient;
import app.nutrimeat.meat.org.nutrimeat.BulkOrderActivity;
import app.nutrimeat.meat.org.nutrimeat.CommonFunctions;
import app.nutrimeat.meat.org.nutrimeat.R;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView_regular;
import app.nutrimeat.meat.org.nutrimeat.api.API;
import app.nutrimeat.meat.org.nutrimeat.product.ModelCart;
import app.nutrimeat.meat.org.nutrimeat.product.Product_Model;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PREORDER_CART;
import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PRODUCT_CART;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner quantity, desired;
    ModelCart model_in_cart = new ModelCart();
    private Product_Model product;
    int position;
    p_MyCustomTextView_regular item_price;
    Products_Details_Model products;
    boolean product_exist;
    int q, d = 0;
    FloatingActionButton fab;
    private boolean isPreOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isPreOrder = getIntent().getBooleanExtra("is_pre_order", false);
        product = (Product_Model) getIntent().getSerializableExtra("product");
        product_exist = (boolean) getIntent().getBooleanExtra("product_exist", false);
        model_in_cart = (ModelCart) getIntent().getSerializableExtra("model_in_cart");
        Log.d("product", product.getItem_name());
        position = getIntent().getIntExtra("position", position);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        ImageView bg_image = (ImageView) findViewById(R.id.product_image);
        quantity = (Spinner) findViewById(R.id.quantity);
        item_price = (p_MyCustomTextView_regular) findViewById(R.id.item_price);
        findViewById(R.id.tvBuilOrder).setOnClickListener(this);
        desired = (Spinner) findViewById(R.id.desired);
        Picasso.with(this)
                .load("http://www.nutrimeat.in/assets/user/img/products/med/" + product.getItem_image())
                //  .resize(dp2px(220), 0)
                .into(bg_image);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ModelCart> set_isadd_to_cart = new ArrayList<>();

                List<ModelCart> isadd_to_cart;

                if (!isPreOrder) {
                    isadd_to_cart = CommonFunctions.getSharedPreferenceProductList(ProductDetailsActivity.this, PREF_PRODUCT_CART);
                } else {
                    isadd_to_cart = CommonFunctions.getSharedPreferenceProductList(ProductDetailsActivity.this, PREF_PREORDER_CART);
                }
                if (isadd_to_cart == null) {
                    ModelCart modelCart_pojo = getmodel(products);
                    set_isadd_to_cart.add(modelCart_pojo);
                    Snackbar.make(view, "Product Added", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    set_isadd_to_cart = isadd_to_cart;
                    if (contains(set_isadd_to_cart, product.getItem_id())) {
                        set_isadd_to_cart = remove(set_isadd_to_cart, product.getItem_id());
                        Snackbar.make(view, "Product Removed", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        ModelCart modelCart_pojo = getmodel(products);
                        set_isadd_to_cart.add(modelCart_pojo);
                        Snackbar.make(view, "Product Added", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
                    }
                }
                if (!isPreOrder) {
                    CommonFunctions.setSharedPreferenceProductList(ProductDetailsActivity.this, PREF_PRODUCT_CART, set_isadd_to_cart);
                } else {
                    CommonFunctions.setSharedPreferenceProductList(ProductDetailsActivity.this, PREF_PREORDER_CART, set_isadd_to_cart);
                }
                onBackPressed();
            }
        });
        collapsingToolbar.setTitle(product.getItem_name());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        API apiService = ApiClient.getClient().create(API.class);
        Call<Products_Details_Model> call = apiService.products_details(String.valueOf(product.getItem_id()));

        call.enqueue(new Callback<Products_Details_Model>() {
            @Override
            public void onResponse(Call<Products_Details_Model> call, Response<Products_Details_Model> response) {
                products = response.body();
                p_MyCustomTextView_regular desciption = (p_MyCustomTextView_regular) findViewById(R.id.desciption);
                desciption.setText(products.getItem_desc().replace("<br>", ""));
                final List<String> quantityList = Arrays.asList(products.getQuantity().split(","));
                final List<String> final_quantityList = new ArrayList<String>();
                for (int i = 0; i < quantityList.size(); i++) {
                    String val = quantityList.get(i) + " " + products.getUnit();
                    final_quantityList.add(val);
                }
                final List<String> desiredList = Arrays.asList(products.getDesired_cut().split(","));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ProductDetailsActivity.this, android.R.layout.simple_spinner_item, final_quantityList);
                ArrayAdapter<String> desireddataAdapter = new ArrayAdapter<String>(ProductDetailsActivity.this, android.R.layout.simple_spinner_item, desiredList);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        Double val = Double.parseDouble(quantityList.get(position));
                        Double act_price = product.getItem_price();
                        Double quantity_price = act_price * val;
                        item_price.setText(String.valueOf(quantity_price));
                        model_in_cart.setSelected_quantity(String.valueOf(val));
//                        products.setQuantity(String.valueOf(quantityList.get(position)));
                        if (product_exist) {
                            if (++q > 0)
                                updateshared();
                        }

                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                        model_in_cart.setSelected_quantity(String.valueOf(quantityList.get(0)));
//                        products.setQuantity(String.valueOf(quantityList.get(0)));
                    }

                });
                // attaching data adapter to spinner
                quantity.setAdapter(dataAdapter);
                // Creating adapter for spinner

                // Drop down layout style - list view with radio button
                desireddataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                desired.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (product_exist) {
                            model_in_cart.setSelected_desired_cut(String.valueOf(desiredList.get(position)));
//                            products.setDesired_cut(String.valueOf(desiredList.get(position)));
                            if (++d > 0)
                                updateshared();
                        }
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
//                        products.setDesired_cut(String.valueOf(desiredList.get(0)));
                        model_in_cart.setSelected_desired_cut(String.valueOf(desiredList.get(0)));
                    }

                });
                // attaching data adapter to spinner
                desired.setAdapter(desireddataAdapter);
                setui_color();
            }

            @Override
            public void onFailure(Call<Products_Details_Model> call, Throwable t) {
                // Log error here since request failed
                Log.e("Products", t.toString());
            }
        });

        //    j++;
    }

    private void setui_color() {
        if (product_exist) {
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            int selected_qt = Integer.parseInt(model_in_cart.getSelected_quantity());
            int selected_der = Integer.parseInt(model_in_cart.getSelected_desired_cut());
            Log.e("Selected product_name", model_in_cart.getItem_name());
            Log.e(" Selected quantity_sel", String.valueOf(quantity.getSelectedItemPosition()));
            Log.e(" Selected desried_sel", String.valueOf(desired.getSelectedItemPosition()));
            quantity.setSelection(selected_qt);
            desired.setSelection(selected_der);


        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void updateshared() {
        List<ModelCart> set_isadd_to_cart;

        if (!isPreOrder) {
            set_isadd_to_cart = CommonFunctions.getSharedPreferenceProductList(ProductDetailsActivity.this, PREF_PRODUCT_CART);
        } else {
            set_isadd_to_cart = CommonFunctions.getSharedPreferenceProductList(ProductDetailsActivity.this, PREF_PREORDER_CART);

        }
        ModelCart modelCart_pojo = getmodel(products);
        int get_pos = getpositonincart(set_isadd_to_cart, modelCart_pojo);
        if (get_pos != 0) {
            get_pos = get_pos - 1;
            set_isadd_to_cart.set(get_pos, modelCart_pojo);
        }
        if (!isPreOrder) {
            CommonFunctions.setSharedPreferenceProductList(ProductDetailsActivity.this, PREF_PRODUCT_CART, set_isadd_to_cart);
        } else {
            CommonFunctions.setSharedPreferenceProductList(ProductDetailsActivity.this, PREF_PREORDER_CART, set_isadd_to_cart);
        }
    }

    private int getpositonincart(List<ModelCart> isadd_to_cart, ModelCart modelCart_pojo) {
        for (int i = 1; i < isadd_to_cart.size() + 1; i++) {

            if (isadd_to_cart.get(i - 1).getItem_id() == modelCart_pojo.getItem_id()) {
                return i;
            }
        }
        return 0;
    }

    private ModelCart getmodel(Products_Details_Model product) {
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
        modelcart.setSelected_desired_cut(String.valueOf(desired.getSelectedItemPosition()));
        modelcart.setSelected_quantity(String.valueOf(quantity.getSelectedItemPosition()));
        Log.e("product_name", modelcart.getItem_name());
        Log.e("quantity_sel", String.valueOf(quantity.getSelectedItemPosition()));
        Log.e("desried_sel", String.valueOf(desired.getSelectedItemPosition()));
        return modelcart;

    }

    boolean contains(List<ModelCart> list, int product_id) {
        for (ModelCart item : list) {
            if (item != null) {
                if (item.getItem_id() == product_id) {
                    return true;
                }
            }
        }
        return false;
    }

    List<ModelCart> remove(List<ModelCart> list, int product_id) {
        for (int i = 0; i < list.size(); i++) {
            ModelCart item = list.get(i);
            //Product_Model item : list) {
            if (item!= null) {
                if (item.getItem_id() == product_id) {
                    list.remove(i);
                }
            }
        }
        return list;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("product", product);
        intent.putExtra("position", position);
        setResult(Activity.RESULT_OK, intent);
        finish();
        //  super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuilOrder:
                startActivity(new Intent(this, BulkOrderActivity.class));
                break;
        }
    }
}
