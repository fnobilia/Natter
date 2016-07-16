package it.natter.classes;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Created by francesco on 25/03/14.
 */

public class Position {

    @SerializedName("id_natter")
    private String id_natter;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public Position(){}

    public Position(String id_natter, String latitude, String longitude) {
        this.id_natter = id_natter;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId_natter() {
        return id_natter;
    }

    public void setId_natter(String id_natter) {
        this.id_natter = id_natter;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public LatLng getLatLng(){
        return new LatLng((new Double(this.latitude)).doubleValue(),(new Double(this.longitude)).doubleValue());
    }

    @Override
    public String toString() {
        return "Position{" +
                "id_natter='" + id_natter + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}