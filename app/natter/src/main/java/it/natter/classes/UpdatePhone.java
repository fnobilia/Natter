package it.natter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francesco on 25/03/14.
 */

public class UpdatePhone {

    @SerializedName("id")
    private int id;

    @SerializedName("phone")
    private String phone;

    @SerializedName("editable")
    private boolean editable;

    public UpdatePhone(int id, String phone, boolean editable){
        this.id = id;
        this.phone = phone;
        this.editable = editable;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isEditable() {
        return editable;
    }
}