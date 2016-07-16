package it.natter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francesco on 25/03/14.
 */

public class Access {

    @SerializedName("id_natter")
    private String id_natter;

    @SerializedName("timestamp")
    private String timestamp;

    public Access() {}

    public Access(String id_natter, String timestamp) {
        this.id_natter = id_natter;
        this.timestamp = timestamp;
    }

    public String getId_natter() {
        return id_natter;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Access{" +
                "id_natter='" + id_natter + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}