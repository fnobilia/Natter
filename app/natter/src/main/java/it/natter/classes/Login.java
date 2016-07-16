package it.natter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francesco on 25/03/14.
 */

public class Login{

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public Login(){}

    public Login(String email,String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}