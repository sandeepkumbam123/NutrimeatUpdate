package app.nutrimeat.meat.org.nutrimeat.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Product_Model implements Serializable {
    @SerializedName("item_id")
    private int item_id;
    @SerializedName("item_name")
    private String item_name;
    @SerializedName("item_price")
    private double item_price;
    @SerializedName("item_image")
    private String item_image;
    @SerializedName("item_cat")
    private String item_cat;

    public Product_Model(int item_id, String item_name, double item_price, String item_image, String item_cat) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_image = item_image;
        this.item_cat = item_cat;
    }

    public void setItem_cat(String item_cat) {
        this.item_cat = item_cat;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_cat() {
        return item_cat;
    }

    public String getItem_image() {
        return item_image;
    }

    public double getItem_price() {
        return item_price;
    }

    public String getItem_name() {
        return item_name;
    }

    public int getItem_id() {
        return item_id;
    }

}
