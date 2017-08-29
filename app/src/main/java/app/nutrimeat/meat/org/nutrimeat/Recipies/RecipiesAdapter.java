package app.nutrimeat.meat.org.nutrimeat.Recipies;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.R;


public class RecipiesAdapter extends RecyclerView.Adapter<RecipiesAdapter.ProductViewHolder> {

    private List<Recipies_Model> products;
    private int rowLayout;
    private FragmentActivity context;

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView product_image;
        TextView item_name;
        TextView item_price;
        Button read_more;

        public ProductViewHolder(View v) {
            super(v);
            product_image = (RoundedImageView) v.findViewById(R.id.product_image);
            item_name = (TextView) v.findViewById(R.id.item_name);
            item_price = (TextView) v.findViewById(R.id.item_time);
            read_more = (Button) v.findViewById(R.id.read_more);

        }
    }

    public RecipiesAdapter(List<Recipies_Model> movies, int rowLayout, FragmentActivity context) {
        this.products = movies;
        this.rowLayout = rowLayout;
        this.context = context;

    }

    @Override
    public RecipiesAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ProductViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        Picasso.with(holder.product_image.getContext())
                .load("http://www.nutrimeat.in/assets/user/wc/" + products.get(position).getRe_img_cover())
                //  .resize(dp2px(220), 0)
                .into(holder.product_image);
        holder.item_name.setText(products.get(position).getRe_name());
        holder.item_price.setText("Preparation time: " + String.valueOf(products.get(position).getRe_prep_time()));
        holder.item_price.setTextSize(12);
        holder.read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecipiesDetailsActivity.class);
                intent.putExtra("recipi_id",products.get(position).getId());
                context.startActivityForResult(intent, 1);
            }
        });

    }


    @Override
    public int getItemCount() {
        return products.size();
    }
}