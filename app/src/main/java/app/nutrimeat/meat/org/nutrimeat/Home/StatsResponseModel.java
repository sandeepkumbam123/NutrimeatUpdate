package app.nutrimeat.meat.org.nutrimeat.Home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 6/7/2017.
 */

public class StatsResponseModel {

    @SerializedName("happycustomers")
    @Expose
    private String happycustomers;
    @SerializedName("sold")
    @Expose
    private String sold;
    @SerializedName("delivered")
    @Expose
    private String delivered;

    public String getHappycustomers() {
        return happycustomers;
    }

    public void setHappycustomers(String happycustomers) {
        this.happycustomers = happycustomers;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    @Override
    public String toString() {
        return "StatsResponseModel{" +
                "happycustomers='" + happycustomers + '\'' +
                ", sold='" + sold + '\'' +
                ", delivered='" + delivered + '\'' +
                '}';
    }
}
