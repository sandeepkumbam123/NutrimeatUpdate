package app.nutrimeat.meat.org.nutrimeat.api;

/**
 * Created by skumbam on 8/31/17.
 */

public class UpdateOrderStatus {
    private String order_no;
    private String reference;

    public String getOrderNumber() {
        return order_no;
    }

    public void setOrderNumber(String orderNumber) {
        this.order_no = orderNumber;
    }

    public String getReferenceNumber() {
        return reference;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.reference = referenceNumber;
    }
}
