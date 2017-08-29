package app.nutrimeat.meat.org.nutrimeat.Recipies;

import com.google.gson.annotations.SerializedName;


public class Recipies_Details_Model {
    @SerializedName("id")
    private int id;
    @SerializedName("re_name")
    private String re_name;

    public String getRe_ingredients() {
        return re_ingredients;
    }

    public void setRe_ingredients(String re_ingredients) {
        this.re_ingredients = re_ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRe_name() {
        return re_name;
    }

    public void setRe_name(String re_name) {
        this.re_name = re_name;
    }

    public String getRe_description() {
        return re_description;
    }

    public void setRe_description(String re_description) {
        this.re_description = re_description;
    }

    public String getRe_img_thumb() {
        return re_img_thumb;
    }

    public void setRe_img_thumb(String re_img_thumb) {
        this.re_img_thumb = re_img_thumb;
    }

    public String getRe_img_cover() {
        return re_img_cover;
    }

    public void setRe_img_cover(String re_img_cover) {
        this.re_img_cover = re_img_cover;
    }

    public String getRe_downlink() {
        return re_downlink;
    }

    public void setRe_downlink(String re_downlink) {
        this.re_downlink = re_downlink;
    }

    public String getRe_prep_description() {
        return re_prep_description;
    }

    public void setRe_prep_description(String re_prep_description) {
        this.re_prep_description = re_prep_description;
    }

    public String getRe_prep_time() {
        return re_prep_time;
    }

    public void setRe_prep_time(String re_prep_time) {
        this.re_prep_time = re_prep_time;
    }

    public String getRe_servings() {
        return re_servings;
    }

    public void setRe_servings(String re_servings) {
        this.re_servings = re_servings;
    }

    public String getRe_cal() {
        return re_cal;
    }

    public void setRe_cal(String re_cal) {
        this.re_cal = re_cal;
    }

    public String getRe_type() {
        return re_type;
    }

    public void setRe_type(String re_type) {
        this.re_type = re_type;
    }

    public String getRe_meat() {
        return re_meat;
    }

    public void setRe_meat(String re_meat) {
        this.re_meat = re_meat;
    }

    @SerializedName("re_ingredients")
    private String re_ingredients;
    @SerializedName("re_description")
    private String re_description;

    @SerializedName("re_img_thumb")
    private String re_img_thumb;
    @SerializedName("re_img_cover")
    private String re_img_cover;
    @SerializedName("re_downlink")
    private String re_downlink;
    @SerializedName("re_prep_description")
    private String re_prep_description;


    @SerializedName("re_prep_time")
    private String re_prep_time;
    @SerializedName("re_servings")
    private String re_servings;
    @SerializedName("re_cal")
    private String re_cal;
    @SerializedName("re_type")
    private String re_type;
    @SerializedName("re_meat")
    private String re_meat;



}
