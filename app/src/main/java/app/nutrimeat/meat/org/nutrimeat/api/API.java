package app.nutrimeat.meat.org.nutrimeat.api;

import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.Account.Orders_Items_Response;
import app.nutrimeat.meat.org.nutrimeat.Account.Orders_Model;
import app.nutrimeat.meat.org.nutrimeat.Account.User_Details_Model;
import app.nutrimeat.meat.org.nutrimeat.Checkout.AddOrderItemResponse;
import app.nutrimeat.meat.org.nutrimeat.Checkout.GenerateOrderNoResponse;
import app.nutrimeat.meat.org.nutrimeat.Home.CheckAreaReponse;
import app.nutrimeat.meat.org.nutrimeat.Home.StatsResponseModel;
import app.nutrimeat.meat.org.nutrimeat.ProductDetails.Products_Details_Model;
import app.nutrimeat.meat.org.nutrimeat.Recipies.Recipies_Details_Model;
import app.nutrimeat.meat.org.nutrimeat.Recipies.Recipies_Model;
import app.nutrimeat.meat.org.nutrimeat.WatsCooking.WatsCooking_Model;
import app.nutrimeat.meat.org.nutrimeat.product.Product_Model;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface API {
    @POST("main/register/format/json")
    Call<ServerResponse> getrespone(@Body Request request);

    @POST("key/login/format/json")
    Call<ServerResponse> getlogin_response(@Body LoginRequest loginRequest);

    @POST("main/change_password_email/format/json")
    Call<ServerResponse> getForgot_response(@Body ForgotpasswordRequest forgotpasswordRequest);

    @POST("main/send_sms/format/json")
    Call<ServerResponse> sendSms(@Body SendSMS sendSMS);

    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @POST ("main/update_order_status/format/json")
    Call<Object> getOrderStatus(@Body UpdateOrderStatus orderRequest );

    @POST("main/verify_sms/format/json")
    Call<ServerResponse> verifysms(@Body VerifySMS verifySMS);

    @POST("main/email_check")
    Call<ServerResponse> checkuser(@Body CheckUser checkUser);

    @POST("main/delivery_service/format/json")
    Call<CheckAreaReponse> checkarea(@Body CheckArea area);

    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @GET("main/products/format/json")
    Call<List<Product_Model>> products(@Query("cat") String cat);

    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @GET("main/products/id/{pid}/format/json")
    Call<Products_Details_Model> products_details(@Path("pid") String pid);

    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @GET("main/whats_cooking/format/json")
    Call<List<WatsCooking_Model>> watscooking();

    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @GET("main/recipes/format/json")
    Call<List<Recipies_Model>> recipies();

    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @GET("main/recipes/id/{rid}/format/json")
    Call<List<Recipies_Details_Model>> recipie_details(@Path("rid") String rid);

    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @GET("main/orders/format/json")
    Call<List<Orders_Model>> ordered_products(@Query("identity") String identity);

    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @GET("main/user_details/format/json")
    Call<User_Details_Model> userDetails(@Query("identity") String identity);

    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @GET("main/orders/format/json")
    Call<Orders_Items_Response> ordered_item_details(@Query("identity") String identity, @Query("order_no") String order_no);

    @GET("main/stats")
    Call<StatsResponseModel> getStats();

    @GET("main/ads")
    Call<String> getAds();

    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @GET("main/order_number/format/json")
    Call<GenerateOrderNoResponse> generateOrderNo();

    @FormUrlEncoded
    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @POST("main/add_order_item/format/json")
    Call<AddOrderItemResponse> addOrderItem(@Field("order_no") String orderNo, @Field("item_id") String itemId,
                                            @Field("item_name") String itemName, @Field("item_option") String itemOption,
                                            @Field("item_price") String itemPrice, @Field("item_weight") String itemWeight,
                                            @Field("item_note") String itemNote);

    @FormUrlEncoded
    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @POST("main/change_order_status/format/json")
    Call<Object> cancelOrder(@Field("OrderId") String orderNo , @Field("OrderStatus") String orderStatus);

    @FormUrlEncoded
    @Headers("X-API-KEY:80w0g4o84wsc4gsc804c08scs00w8co4wscg848c")
    @POST("main/add_order/format/json")
    Call<AddOrderItemResponse> addOrder(
            @Field("order_no") String orderNo,
            @Field("email") String email,
            @Field("order_total_no_discount") int orderTotalDiscount,
            @Field("discount_desc") String discountDesc,
            @Field("discount_amount") float discountAmount,
            @Field("order_total") double orderTotal,
            @Field("items") int items,
            @Field("total_weight") int totalWeight,
            @Field("ord_status") int orderStatus,
            @Field("ord_date_time") String orderDateTime,
            @Field("ord_name") String orderName,
            @Field("ord_address") String orderAddress,
            @Field("ord_city") String orderCity,
            @Field("ord_state") String orderState,
            @Field("ord_country") String orderCountry,
            @Field("ord_postal") String postal,
            @Field("ord_store") String orderStore,
            @Field("ord_phone") String phone,
            @Field("ord_payment_mode") String paymentMode,
            @Field("ord_type") String orderType,
            @Field("ord_pre_date") String orderPreDate,
            @Field("ord_pre_time") String orderPreTime,
            @Field("ord_online_transaction") String onlineTransaction ,
            @Field("ord_user_longitude ") String userLongitude ,
            @Field("ord_user_latitude") String userLatitude );

}
