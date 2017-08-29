package app.nutrimeat.meat.org.nutrimeat.Checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddOrderItemResponse {

@SerializedName("orderno")
@Expose
private String status;

public String getOrderno() {
return status;
}

public void setOrderno(String status) {
this.status = status;
}

}