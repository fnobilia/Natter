package it.natter.classes;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import it.natter.utility.Code;

/**
 * Created by francesco on 28/03/14.
 */
public class SignIn{
    @SerializedName("id")
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    @SerializedName("surname")
    private String surname;

    @SerializedName("phone")
    private String phone;

    @SerializedName("flag")
    private boolean[] flag;

    @SerializedName("image")
    private String image;

    @SerializedName("social")
    private String social;

    public SignIn(){}

    public SignIn(String id){
        this.id = id;
    }

    public SignIn(String email, String password, String name, String surname, String phone, boolean[] flag,String image,String social){
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.flag = flag;
        this.image = image;
        this.social = social;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public boolean[] getFlag() {
        return flag;
    }

    public String getImage() {
        return image;
    }

    public String getSocial() {
        return social;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getId(){
        return id;
    }

    @Override
    public String toString() {

        String temp;
        if(image.equals(Code.NOT_STATED)){
            temp = Code.NOT_STATED;
        }
        else{
            temp = "#FILE#";
        }

        return "SignIn{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", flag=" + Arrays.toString(flag) +
                ", image= '" + temp + '\'' +
                ", social='" + social + '\'' +
                '}';

    }
}
