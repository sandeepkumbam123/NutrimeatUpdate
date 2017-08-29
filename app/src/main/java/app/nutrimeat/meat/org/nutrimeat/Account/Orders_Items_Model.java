package app.nutrimeat.meat.org.nutrimeat.Account;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Orders_Items_Model implements Serializable {
    @SerializedName("ord_det_item_fk")
    private int ord_det_item_fk;
    @SerializedName("ord_det_item_name")
    private String ord_det_item_name;
    @SerializedName("ord_det_item_option")
    private String ord_det_item_option;
    @SerializedName("ord_det_price")
    private String ord_det_price;
    @SerializedName("ord_det_weight")
    private String ord_det_weight;
    @SerializedName("ord_det_demo_user_note")
    private String ord_det_demo_user_note;

    public int getOrd_det_item_fk() {
        return ord_det_item_fk;
    }

    public void setOrd_det_item_fk(int ord_det_item_fk) {
        this.ord_det_item_fk = ord_det_item_fk;
    }

    public String getOrd_det_item_name() {
        return ord_det_item_name;
    }

    public void setOrd_det_item_name(String ord_det_item_name) {
        this.ord_det_item_name = ord_det_item_name;
    }

    public String getOrd_det_item_option() {
        return ord_det_item_option;
    }

    public void setOrd_det_item_option(String ord_det_item_option) {
        this.ord_det_item_option = ord_det_item_option;
    }

    public String getOrd_det_price() {
        return ord_det_price;
    }

    public void setOrd_det_price(String ord_det_price) {
        this.ord_det_price = ord_det_price;
    }

    public String getOrd_det_weight() {
        return ord_det_weight;
    }

    public void setOrd_det_weight(String ord_det_weight) {
        this.ord_det_weight = ord_det_weight;
    }

    public String getOrd_det_demo_user_note() {
        return ord_det_demo_user_note;
    }

    public void setOrd_det_demo_user_note(String ord_det_demo_user_note) {
        this.ord_det_demo_user_note = ord_det_demo_user_note;
    }

    public String getOrd_det_price_total() {
        return ord_det_price_total;
    }

    public void setOrd_det_price_total(String ord_det_price_total) {
        this.ord_det_price_total = ord_det_price_total;
    }

    @SerializedName("ord_det_price_total")
    private String ord_det_price_total;





}
