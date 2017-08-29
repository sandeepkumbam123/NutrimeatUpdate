package app.nutrimeat.meat.org.nutrimeat.api;


public class VerifySMS {

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    private String mobile;

    private String otp;
}