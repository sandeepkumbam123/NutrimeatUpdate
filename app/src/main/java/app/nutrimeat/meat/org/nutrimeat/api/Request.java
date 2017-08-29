package app.nutrimeat.meat.org.nutrimeat.api;


public class Request {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getMobile() {
        return phone;
    }

    public void setMobile(String mobile) {
        this.phone = mobile;
    }

    private String phone;
}