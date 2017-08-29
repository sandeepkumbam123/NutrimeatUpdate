package app.nutrimeat.meat.org.nutrimeat.Account;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class Orders_Items_Response implements Serializable {
    @SerializedName("ord_order_number")
    private int ord_order_number;
    @SerializedName("ord_user_fk")
    private String ord_user_fk;
    @SerializedName("ord_summary_discount_desc")
    private String ord_summary_discount_desc;
    @SerializedName("ord_item_summary_total")
    private double ord_item_summary_total;
    @SerializedName("ord_savings_total")
    private double ord_savings_total;
    @SerializedName("ord_total")
    private double ord_total;
    @SerializedName("ord_total_items")
    private double ord_total_items;
    @SerializedName("ord_total_weight")
    private double ord_total_weight;
    @SerializedName("ord_status")
    private double ord_status;
    @SerializedName("ord_date")
    private String ord_date;

    @SerializedName("ord_demo_bill_name")
    private String ord_demo_bill_name;
    @SerializedName("ord_demo_bill_address_01")
    private String ord_demo_bill_address_01;
    @SerializedName("ord_demo_bill_city")
    private String ord_demo_bill_city;
    @SerializedName("ord_demo_bill_state")
    private String ord_demo_bill_state;
    @SerializedName("ord_demo_bill_store")
    private String ord_demo_bill_store;
    @SerializedName("ord_demo_bill_post_code")
    private String ord_demo_bill_post_code;
    @SerializedName("ord_demo_bill_country")
    private String ord_demo_bill_country;
    @SerializedName("ord_demo_phone")
    private String ord_demo_phone;
    @SerializedName("ord_payment_mode")
    private String ord_payment_mode;
    @SerializedName("ord_type")
    private String ord_type;
    @SerializedName("ord_pre_date")
    private String ord_pre_date;
    @SerializedName("ord_pre_time")
    private String ord_pre_time;

    public int getOrd_order_number() {
        return ord_order_number;
    }

    public void setOrd_order_number(int ord_order_number) {
        this.ord_order_number = ord_order_number;
    }

    public String getOrd_user_fk() {
        return ord_user_fk;
    }

    public void setOrd_user_fk(String ord_user_fk) {
        this.ord_user_fk = ord_user_fk;
    }

    public String getOrd_summary_discount_desc() {
        return ord_summary_discount_desc;
    }

    public void setOrd_summary_discount_desc(String ord_summary_discount_desc) {
        this.ord_summary_discount_desc = ord_summary_discount_desc;
    }

    public double getOrd_item_summary_total() {
        return ord_item_summary_total;
    }

    public void setOrd_item_summary_total(double ord_item_summary_total) {
        this.ord_item_summary_total = ord_item_summary_total;
    }

    public double getOrd_savings_total() {
        return ord_savings_total;
    }

    public void setOrd_savings_total(double ord_savings_total) {
        this.ord_savings_total = ord_savings_total;
    }

    public double getOrd_total() {
        return ord_total;
    }

    public void setOrd_total(double ord_total) {
        this.ord_total = ord_total;
    }

    public double getOrd_total_items() {
        return ord_total_items;
    }

    public void setOrd_total_items(double ord_total_items) {
        this.ord_total_items = ord_total_items;
    }

    public double getOrd_total_weight() {
        return ord_total_weight;
    }

    public void setOrd_total_weight(double ord_total_weight) {
        this.ord_total_weight = ord_total_weight;
    }

    public double getOrd_status() {
        return ord_status;
    }

    public void setOrd_status(double ord_status) {
        this.ord_status = ord_status;
    }

    public String getOrd_date() {
        return ord_date;
    }

    public void setOrd_date(String ord_date) {
        this.ord_date = ord_date;
    }

    public String getOrd_demo_bill_name() {
        return ord_demo_bill_name;
    }

    public void setOrd_demo_bill_name(String ord_demo_bill_name) {
        this.ord_demo_bill_name = ord_demo_bill_name;
    }

    public String getOrd_demo_bill_address_01() {
        return ord_demo_bill_address_01;
    }

    public void setOrd_demo_bill_address_01(String ord_demo_bill_address_01) {
        this.ord_demo_bill_address_01 = ord_demo_bill_address_01;
    }

    public String getOrd_demo_bill_city() {
        return ord_demo_bill_city;
    }

    public void setOrd_demo_bill_city(String ord_demo_bill_city) {
        this.ord_demo_bill_city = ord_demo_bill_city;
    }

    public String getOrd_demo_bill_state() {
        return ord_demo_bill_state;
    }

    public void setOrd_demo_bill_state(String ord_demo_bill_state) {
        this.ord_demo_bill_state = ord_demo_bill_state;
    }

    public String getOrd_demo_bill_store() {
        return ord_demo_bill_store;
    }

    public void setOrd_demo_bill_store(String ord_demo_bill_store) {
        this.ord_demo_bill_store = ord_demo_bill_store;
    }

    public String getOrd_demo_bill_post_code() {
        return ord_demo_bill_post_code;
    }

    public void setOrd_demo_bill_post_code(String ord_demo_bill_post_code) {
        this.ord_demo_bill_post_code = ord_demo_bill_post_code;
    }

    public String getOrd_demo_bill_country() {
        return ord_demo_bill_country;
    }

    public void setOrd_demo_bill_country(String ord_demo_bill_country) {
        this.ord_demo_bill_country = ord_demo_bill_country;
    }

    public String getOrd_demo_phone() {
        return ord_demo_phone;
    }

    public void setOrd_demo_phone(String ord_demo_phone) {
        this.ord_demo_phone = ord_demo_phone;
    }

    public String getOrd_payment_mode() {
        return ord_payment_mode;
    }

    public void setOrd_payment_mode(String ord_payment_mode) {
        this.ord_payment_mode = ord_payment_mode;
    }

    public String getOrd_type() {
        return ord_type;
    }

    public void setOrd_type(String ord_type) {
        this.ord_type = ord_type;
    }

    public String getOrd_pre_date() {
        return ord_pre_date;
    }

    public void setOrd_pre_date(String ord_pre_date) {
        this.ord_pre_date = ord_pre_date;
    }

    public String getOrd_pre_time() {
        return ord_pre_time;
    }

    public void setOrd_pre_time(String ord_pre_time) {
        this.ord_pre_time = ord_pre_time;
    }

    public String getOrd_payment_transaction() {
        return ord_payment_transaction;
    }

    public void setOrd_payment_transaction(String ord_payment_transaction) {
        this.ord_payment_transaction = ord_payment_transaction;
    }

    @SerializedName("ord_payment_transaction")
    private String ord_payment_transaction;


    public List<Orders_Items_Model> getItems() {
        return items;
    }

    public void setItems(List<Orders_Items_Model> items) {
        this.items = items;
    }

    @SerializedName("items")
    private List<Orders_Items_Model> items;
}
