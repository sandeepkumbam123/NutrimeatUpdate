package app.nutrimeat.meat.org.nutrimeat.ProductDetails;

import com.google.gson.annotations.SerializedName;


public class Products_Details_Model {
    @SerializedName("item_id")
    private int item_id;
    @SerializedName("item_name")
    private String item_name;
    @SerializedName("item_price")
    private double item_price;
    @SerializedName("item_desc")
    private String item_desc;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("desired_cut")
    private String desired_cut;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDesired_cut() {
        return desired_cut;
    }

    public void setDesired_cut(String desired_cut) {
        this.desired_cut = desired_cut;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    @SerializedName("unit")
    private String unit;


    public Products_Details_Model(int item_id, String item_name, double item_price, String item_image, String item_cat,String item_desc,String desired_cut,String unit,String quantity) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_image = item_image;
        this.item_cat = item_cat;
        this.item_desc=item_desc;
        this.desired_cut=desired_cut;
        this.unit=unit;
        this.quantity=quantity;
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

    @SerializedName("item_image")
    private String item_image;

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

    @SerializedName("item_cat")
    private String item_cat;

}
