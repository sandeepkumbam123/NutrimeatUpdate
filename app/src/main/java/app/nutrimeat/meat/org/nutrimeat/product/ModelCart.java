package app.nutrimeat.meat.org.nutrimeat.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelCart implements Serializable {

    private int item_id;
    private String item_name;
    private double item_price;
    private String item_desc;
    private String quantity;
    private String desired_cut;
    private String unit;
    private String item_image;
    private String item_cat;
    private String selected_quantity;
    private String selected_desired_cut;
    public String getSelected_desired_cut() {
        return selected_desired_cut;
    }

    public void setSelected_desired_cut(String selected_desired_cut) {
        this.selected_desired_cut = selected_desired_cut;
    }

    public String getSelected_quantity() {
        return selected_quantity;
    }

    public void setSelected_quantity(String selected_quantity) {
        this.selected_quantity = selected_quantity;
    }



    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


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
