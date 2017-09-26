package app.nutrimeat.meat.org.nutrimeat.Account;

import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;

import java.text.DecimalFormat;
import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.ApiClient;
import app.nutrimeat.meat.org.nutrimeat.R;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView;
import app.nutrimeat.meat.org.nutrimeat.Textview.p_MyCustomTextView_mbold;
import app.nutrimeat.meat.org.nutrimeat.api.API;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ProductViewHolder> {

    private static final String CANCEL_ORDER ="CANCEL_ORDER";
    private List<Orders_Model> products;
    private int rowLayout;
    private AcountFragment context;
    List<Orders_Items_Model> items;
    String User_name;

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        p_MyCustomTextView_mbold order_date;
        p_MyCustomTextView_mbold ordernumber;
        p_MyCustomTextView_mbold item_name;
        p_MyCustomTextView_mbold item_price;
        p_MyCustomTextView status_text;
        CardView cardview;

        public ProductViewHolder(View v) {
            super(v);
            order_date = (p_MyCustomTextView_mbold) v.findViewById(R.id.order_date);
            ordernumber = (p_MyCustomTextView_mbold) v.findViewById(R.id.ordernumber);
            item_name = (p_MyCustomTextView_mbold) v.findViewById(R.id.item_name);
            item_price = (p_MyCustomTextView_mbold) v.findViewById(R.id.item_price);
            status_text = (p_MyCustomTextView) v.findViewById(R.id.status_text);
            cardview = (CardView) v.findViewById(R.id.cv);
        }
    }

    public OrdersAdapter(List<Orders_Model> movies, int rowLayout, AcountFragment context, String user_name) {
        this.products = movies;
        this.rowLayout = rowLayout;
        this.context = context;

        this.User_name = user_name;
    }

    private void getitem_details(String user_name, String order_id, final LinearLayout items_holder) {
        final API apiService = ApiClient.getClient().create(API.class);
        Call<Orders_Items_Response> call = apiService.ordered_item_details(user_name, order_id);
        call.enqueue(new Callback<Orders_Items_Response>() {
            @Override
            public void onResponse(Call<Orders_Items_Response> call, Response<Orders_Items_Response> response) {
                Orders_Items_Response resp=response.body();
                List<Orders_Items_Model> products = resp.getItems();
                for (int i = 0; i < products.size(); i++) {
                    Orders_Items_Model item_model = products.get(i);
                    View items_view = context.getActivity().getLayoutInflater().inflate(R.layout.ordered_item, null);
                    p_MyCustomTextView_mbold item_name = (p_MyCustomTextView_mbold) items_view.findViewById(R.id.item_name);
                    p_MyCustomTextView_mbold item_kgs = (p_MyCustomTextView_mbold) items_view.findViewById(R.id.item_kgs);
                    p_MyCustomTextView_mbold item_price = (p_MyCustomTextView_mbold) items_view.findViewById(R.id.item_price);
                    item_name.setText(item_model.getOrd_det_item_name());
                    item_kgs.setText(item_model.getOrd_det_weight());
                    item_price.setText(item_model.getOrd_det_price_total());
                    items_holder.addView(items_view);
                }
            }

            @Override
            public void onFailure(Call<Orders_Items_Response> call, Throwable t) {
                // Log error here since request failed
                Log.e("Products", t.toString());
            }
        });
    }

    private void dataIsReady(List<Orders_Items_Model> items) {
        this.items = items;
        //do what you want with movies
    }

    @Override
    public OrdersAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ProductViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final Orders_Model product = products.get(position);
        DecimalFormat df = new DecimalFormat("##");
        holder.ordernumber.setText("Order Number : \n" + String.valueOf(product.getOrd_order_number()));
        holder.order_date.setText("Order Date : \n" + product.getOrd_date());
        holder.item_name.setText("Store : \n" + product.getOrd_demo_bill_store());
        holder.item_price.setText("Price : \n" + String.valueOf(product.getOrd_total()) + " for " + String.valueOf(df.format(product.getOrd_total_items())) + " items");
        holder.status_text.setText(String.valueOf(product.getOrd_status()));
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(product, User_name);
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void showDialog(final Orders_Model product, String user_name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context.getContext());
        View view = context.getActivity().getLayoutInflater().inflate(R.layout.orderdetails, null);
        DecimalFormat df = new DecimalFormat("##");
        p_MyCustomTextView_mbold username = (p_MyCustomTextView_mbold) view.findViewById(R.id.username);
        p_MyCustomTextView_mbold useraddress = (p_MyCustomTextView_mbold) view.findViewById(R.id.useraddress);
        p_MyCustomTextView_mbold email = (p_MyCustomTextView_mbold) view.findViewById(R.id.email);
        p_MyCustomTextView_mbold phone = (p_MyCustomTextView_mbold) view.findViewById(R.id.phone);
        LinearLayout items_holder = (LinearLayout) view.findViewById(R.id.items_holder);

        //  p_MyCustomTextView_mbold order_date = (p_MyCustomTextView_mbold) view.findViewById(R.id.order_date);
        p_MyCustomTextView_mbold total_weight = (p_MyCustomTextView_mbold) view.findViewById(R.id.total_weight);
        p_MyCustomTextView_mbold main_total_price = (p_MyCustomTextView_mbold) view.findViewById(R.id.main_total_price);
        p_MyCustomTextView_mbold order_type = (p_MyCustomTextView_mbold) view.findViewById(R.id.order_type);
        p_MyCustomTextView_mbold payment_method = (p_MyCustomTextView_mbold) view.findViewById(R.id.payment_method);
        username.setText("Name : " + product.getOrd_demo_bill_name());
        useraddress.setText("Address : " + product.getOrd_demo_bill_address_01());
        email.setText("Email : " + product.getOrd_user_fk());
        phone.setText("Phone Number : " + product.getOrd_demo_phone());
        total_weight.setText("Total Weight : " + df.format(product.getOrd_total_weight()));
        main_total_price.setText("Total Price : " + String.valueOf(product.getOrd_total()));
        order_type.setText("Order Type: " + String.valueOf(product.getOrd_type()));
        payment_method.setText("Payment Method : " + String.valueOf(product.getOrd_payment_mode()));
        getitem_details(user_name, String.valueOf(product.getOrd_order_number()), items_holder);
        builder.setView(view)
                .setPositiveButton("OK", null)
                /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      if(product.getOrd_status() < 4) {
                          cancelOrder(product.getOrd_order_number() + "");
                      } else {
                          Toast.makeText(context.getContext(), "order "+product.getOrd_order_number()+" can't be cancelled at this point of time. ", Toast.LENGTH_SHORT).show();
                      }
                    }
                })*/;

        AlertDialog dialog = builder.create();
        dialog.show();
    }


   /* private void cancelOrder(final String orderId ) {
        final API apiService = ApiClient.getClient().create(API.class);
        Call<Object> call = apiService.cancelOrder(orderId, CANCEL_ORDER);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("Cancel Response : ", response.body().toString());
                if (((LinkedTreeMap) response.body()).get("status").equals("failed")) {
                    Toast.makeText(context.getContext(), "order " + orderId + " can't be cancelled at this point of time. ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getContext(), "order " + orderId + " has been successfully cancelled . ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(context.getContext(), "order "+orderId+" can't be cancelled at this point of time. ", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}