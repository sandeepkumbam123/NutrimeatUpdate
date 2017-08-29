package app.nutrimeat.meat.org.nutrimeat.Account;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kumbh on 29-08-2017.
 */

public class User_Details_Model implements Serializable {
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phoneNumber;
    @SerializedName("first_name")
    private String userName;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
