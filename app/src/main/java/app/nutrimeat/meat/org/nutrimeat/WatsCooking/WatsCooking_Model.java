package app.nutrimeat.meat.org.nutrimeat.WatsCooking;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class WatsCooking_Model implements Serializable {
    @SerializedName("item_id")
    private int item_id;
    @SerializedName("name")
    private String name;
    @SerializedName("people2")
    private double people2;
    @SerializedName("people4")
    private double people4;
    @SerializedName("people6")
    private double people6;
    @SerializedName("recipe_link")
    private String recipe_link;
    @SerializedName("cut")
    private String cut;
    @SerializedName("prep_desc")
    private String prep_desc;
    @SerializedName("image")
    private String image;

    @SerializedName("prep_time")
    private String prep_time;
    @SerializedName("type")
    private String type;

    public String getMeat() {
        return meat;
    }

    public void setMeat(String meat) {
        this.meat = meat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(String prep_time) {
        this.prep_time = prep_time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrep_desc() {
        return prep_desc;
    }

    public void setPrep_desc(String prep_desc) {
        this.prep_desc = prep_desc;
    }

    public String getCut() {
        return cut;
    }

    public void setCut(String cut) {
        this.cut = cut;
    }

    public String getRecipe_link() {
        return recipe_link;
    }

    public void setRecipe_link(String recipe_link) {
        this.recipe_link = recipe_link;
    }

    public double getPeople6() {
        return people6;
    }

    public void setPeople6(double people6) {
        this.people6 = people6;
    }

    public double getPeople4() {
        return people4;
    }

    public void setPeople4(double people4) {
        this.people4 = people4;
    }

    public double getPeople2() {
        return people2;
    }

    public void setPeople2(double people2) {
        this.people2 = people2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    @SerializedName("meat")
    private String meat;
}
