package app.nutrimeat.meat.org.nutrimeat.WatsCooking;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.ApiClient;
import app.nutrimeat.meat.org.nutrimeat.CommonFunctions;
import app.nutrimeat.meat.org.nutrimeat.Navdrawer;
import app.nutrimeat.meat.org.nutrimeat.ProductDetails.Products_Details_Model;
import app.nutrimeat.meat.org.nutrimeat.R;
import app.nutrimeat.meat.org.nutrimeat.Recipies.RecipiesDetailsActivity;
import app.nutrimeat.meat.org.nutrimeat.api.API;
import app.nutrimeat.meat.org.nutrimeat.product.ModelCart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PREORDER_CART;
import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PRODUCT_CART;


public class WatsCookingAdapter extends RecyclerView.Adapter<WatsCookingAdapter.ProductViewHolder> {

    private List<WatsCooking_Model> products;
    private int rowLayout;
    private WatsCookingFragment context;
    int serv_position;

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView product_image;
        TextView item_name;
        TextView item_price;
        Button buynow, recipies;
        RadioGroup radioGroup;

        public ProductViewHolder(View v) {
            super(v);
            product_image = (RoundedImageView) v.findViewById(R.id.product_image);
            item_name = (TextView) v.findViewById(R.id.item_name);
            item_price = (TextView) v.findViewById(R.id.item_time);
            buynow = (Button) v.findViewById(R.id.buynow);
            recipies = (Button) v.findViewById(R.id.recipies);
            radioGroup = (RadioGroup) v.findViewById(R.id.myRadioGroup);
        }
    }

    public WatsCookingAdapter(List<WatsCooking_Model> movies, int rowLayout, WatsCookingFragment context) {
        this.products = movies;
        this.rowLayout = rowLayout;
        this.context = context;


    }

    @Override
    public WatsCookingAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, null, false);
        return new ProductViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        Picasso.with(holder.product_image.getContext())
                .load("http://www.nutrimeat.in/assets/user/wc/" + products.get(position).getImage())
                //  .resize(dp2px(220), 0)
                .into(holder.product_image);
        final List<ModelCart> preorderList = CommonFunctions.getSharedPreferenceProductList(context.getActivity(), PREF_PREORDER_CART);
        holder.item_name.setText(products.get(position).getName());
        holder.item_price.setText("Preparation time: " + String.valueOf(products.get(position).getPrep_time()));
        holder.item_price.setTextSize(12);
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                serv_position = radioGroup.indexOfChild(radioButton);
            }
        });
        holder.buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(context instanceof WatsCookingFragment){
                    if(preorderList.size()==0) {
                        ((WatsCookingFragment) context).add_product(products, position, serv_position);
                    }
                }

            }
        });
        holder.recipies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = products.get(position).getRecipe_link();
                String fileName = path.substring(path.lastIndexOf("id=") + 1);
                fileName = fileName.replace("d=", "");
                Intent intent = new Intent(context.getActivity(), RecipiesDetailsActivity.class);
                intent.putExtra("recipi_id", Integer.parseInt(fileName));
                context.startActivityForResult(intent, 1);
            }
        });

    }





    @Override
    public int getItemCount() {
        return products.size();
    }
}