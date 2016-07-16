package it.natter.classes;

import com.google.gson.annotations.SerializedName;

import it.natter.utility.Code;

/**
 * Created by francesco on 25/03/14.
 */

public class ContactService {

    @SerializedName("id_natter")
    private String id_natter = "";

    @SerializedName("email")
    private String email = "";

    @SerializedName("name")
    private String name = "";

    @SerializedName("surname")
    private String surname = "";

    @SerializedName("phone")
    private String phone = "";

    @SerializedName("photo")
    private String photo = "";

    @SerializedName("hasPhoto")
    private boolean hasPhoto = false;

    public ContactService(){}

    public String getId_natter() {
        return id_natter;
    }

    public void setId_natter(String id_natter) {
        this.id_natter = id_natter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isHasPhoto() {
        return hasPhoto;
    }

    public void setHasPhoto(boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }

    @Override
    public String toString() {

        String temp = Code.NOT_STATED;
        if(!this.photo.equals(Code.NOT_STATED)){
            temp = "#FILE#";
        }

        return "ContactService{" +
                "id_natter='" + id_natter + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + temp + '\'' +
                ", hasPhoto=" + hasPhoto +
                '}';
    }
}