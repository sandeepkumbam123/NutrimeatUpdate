package app.nutrimeat.meat.org.nutrimeat.Checkout;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.CommonFunctions;
import app.nutrimeat.meat.org.nutrimeat.R;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView_mbold;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView_regular;
import app.nutrimeat.meat.org.nutrimeat.product.ModelCart;

import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PREORDER_CART;
import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PRODUCT_CART;


public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ProductViewHolder> {

    private int rowLayout;
    private CheckoutActivity context;
    ArrayList<ModelCart> listofProdcuts = new ArrayList<>();
    private double sub_total = 0;

    public CheckoutAdapter(ArrayList<ModelCart> listofProdcuts, int checkout_item, CheckoutActivity checkoutActivity) {
        this.listofProdcuts = listofProdcuts;
        this.rowLayout = checkout_item;
        this.context = checkoutActivity;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        p_MyCustomTextView_mbold product_name;
        p_MyCustomTextView_regular item_price_;
        Spinner quantity_sp;
        ImageView item_pic;
        LinearLayout delete;

        public ProductViewHolder(View v) {
            super(v);
            item_pic = (ImageView) v.findViewById(R.id.item_pic);
            item_price_ = (p_MyCustomTextView_regular) v.findViewById(R.id.item_price);
            product_name = (p_MyCustomTextView_mbold) v.findViewById(R.id.product_name);
            quantity_sp = (Spinner) v.findViewById(R.id.quantity_sp);
            delete = (LinearLayout) v.findViewById(R.id.delete);

        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final ModelCart product = listofProdcuts.get(position);
        Picasso.with(holder.item_pic.getContext())
                .load("http://www.nutrimeat.in/assets/user/img/products/med/" + product.getItem_image())
                //  .resize(dp2px(220), 0)
                .into(holder.item_pic);
        holder.product_name.setText(product.getItem_name());
        final List<String> quantityList = Arrays.asList(product.getQuantity().split(","));
        final List<String> desirecutList = Arrays.asList(product.getDesired_cut().split(","));
        final List<String> final_quantityList = new ArrayList<String>();
        for (int i = 0; i < quantityList.size(); i++) {
            String val_ = quantityList.get(i) + " " + product.getUnit();
            final_quantityList.add(val_);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, final_quantityList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.quantity_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int sp_position, long id) {
                // qty_selected = position;
                Double val = Double.parseDouble(quantityList.get(sp_position));
                Double act_price = product.getItem_price();
                Double quantity_price = act_price * val;
                holder.item_price_.setText(String.valueOf(quantity_price));
                if (context.price.size() < position + 1) {
                    context.price.add(position, quantity_price);
                } else {
                    context.price.set(position, quantity_price);
                }
                //
                setPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        // attaching data adapter to spinner
        holder.quantity_sp.setAdapter(dataAdapter);
        Log.e(product.getItem_name(), product.getSelected_quantity());
        holder.quantity_sp.setSelection(Integer.parseInt(product.getSelected_quantity()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position);
            }
        });

    }

    private void setPrice() {
        sub_total = 0;
        for (int i = 0; i < context.price.size(); i++) {
            sub_total += context.price.get(i);
        }
        context.subtotal.setText(String.valueOf(sub_total));
    }

    public double getSub_total() {
        return sub_total;
    }

    @Override
    public int getItemCount() {
        return listofProdcuts.size();
    }

    private boolean isPreorder() {
        List<ModelCart> sharedPreferenceProductList = CommonFunctions.getSharedPreferenceProductList(context, PREF_PREORDER_CART);
        if (sharedPreferenceProductList.size() > 0) {
            return true;
        }

        return false;
    }

    public void removeAt(int position) {
        if (listofProdcuts.size() > position)
            listofProdcuts.remove(position);
        List<ModelCart> set_isadd_to_cart = new ArrayList<>();
        set_isadd_to_cart.addAll(listofProdcuts);

        if (isPreorder()) {
            CommonFunctions.setSharedPreferenceProductList(context, PREF_PREORDER_CART, set_isadd_to_cart);
        } else {
            CommonFunctions.setSharedPreferenceProductList(context, PREF_PRODUCT_CART, set_isadd_to_cart);
        }
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listofProdcuts.size());
        context.price.remove(position);
        setPrice();

    }
}