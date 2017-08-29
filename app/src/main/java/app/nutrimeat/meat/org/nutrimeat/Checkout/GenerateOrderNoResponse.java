package app.nutrimeat.meat.org.nutrimeat.Checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateOrderNoResponse {

@SerializedName("orderno")
@Expose
private String orderno;

public String getOrderno() {
return orderno;
}

public void setOrderno(String orderno) {
this.orderno = orderno;
}

}