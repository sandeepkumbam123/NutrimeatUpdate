package app.nutrimeat.meat.org.nutrimeat.Home;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class CheckAreaReponse implements Serializable {
    @SerializedName("status")
    private String status;
    @SerializedName("error")
    private String error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    private String message;


}
