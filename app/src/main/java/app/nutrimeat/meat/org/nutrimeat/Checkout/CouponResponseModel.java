package app.nutrimeat.meat.org.nutrimeat.Checkout;


import com.google.gson.annotations.SerializedName;

/**
 * Created by skumbam on 10/20/17.
 */

public class CouponResponseModel  {

    @SerializedName("status")
    private String mSuccess;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("data")
    private CouponDetails data;


    public String getmSuccess() {
        return mSuccess;
    }

    public void setmSuccess(String mSuccess) {
        this.mSuccess = mSuccess;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public CouponDetails getData() {
        return data;
    }

    public void setData(CouponDetails data) {
        this.data = data;
    }

    public CouponResponseModel(String mSuccess, String mMessage, CouponDetails data) {

        this.mSuccess = mSuccess;
        this.mMessage = mMessage;
        this.data = data;
    }

    public class CouponDetails {
        @SerializedName("coupoun_method")
        private String couponType;
        @SerializedName("coupoun_description")
        private String couponDescription;
        @SerializedName("min_cart_value")
        private String minCartValue;
        @SerializedName("max_value")
        private String maxCartValue ;

        public CouponDetails(String couponType, String couponDescription, String minCartValue, String maxCartValue) {
            this.couponType = couponType;
            this.couponDescription = couponDescription;
            this.minCartValue = minCartValue;
            this.maxCartValue = maxCartValue;
        }

        public String getCouponType() {
            return couponType;
        }

        public void setCouponType(String couponType) {
            this.couponType = couponType;
        }

        public String getCouponDescription() {
            return couponDescription;
        }

        public void setCouponDescription(String couponDescription) {
            this.couponDescription = couponDescription;
        }

        public String getMinCartValue() {
            return minCartValue;
        }

        public void setMinCartValue(String minCartValue) {
            this.minCartValue = minCartValue;
        }

        public String getMaxCartValue() {
            return maxCartValue;
        }

        public void setMaxCartValue(String maxCartValue) {
            this.maxCartValue = maxCartValue;
        }
    }
}
