package it.natter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francesco on 25/03/14.
 */

public class UpdateEmail {

    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("editable")
    private boolean editable;

    public UpdateEmail(int id, String email, boolean editable){
        this.id = id;
        this.email = email;
        this.editable = editable;
    }

    public int getId(){
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEditable() {
        return editable;
    }
}