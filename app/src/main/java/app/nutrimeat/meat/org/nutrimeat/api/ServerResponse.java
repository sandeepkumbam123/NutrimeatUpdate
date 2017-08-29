package app.nutrimeat.meat.org.nutrimeat.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class ServerResponse {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private String error;

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

}
