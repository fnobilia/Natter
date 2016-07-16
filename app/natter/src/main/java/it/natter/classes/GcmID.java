package it.natter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francesco on 22/04/14.
 */
public class GcmID {

    @SerializedName("id_natter")
    private String id_natter;

    @SerializedName("id_gcm")
    private String id_gcm;

    public GcmID(String natter, String gcm){
        this.id_natter = natter;
        this.id_gcm = gcm;
    }

    public GcmID(int natter, String gcm){
        this.id_natter = (new Integer(natter)).toString();
        this.id_gcm = gcm;
    }

    public String getId_natter() {
        return id_natter;
    }

    public void setId_natter(String id_natter) {
        this.id_natter = id_natter;
    }

    public String getId_gcm() {
        return id_gcm;
    }

    public void setId_gcm(String id_gcm) {
        this.id_gcm = id_gcm;
    }

    @Override
    public String toString() {
        return "GcmID [id_natter=" + id_natter + ", id_gcm=" + id_gcm + "]";
    }


}
