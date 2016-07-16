package it.natter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francesco on 25/03/14.
 */

public class ImageService{

    @SerializedName("id")
    private String id;

    @SerializedName("image")
    private String image;

    public ImageService(){}

    public ImageService(String id, String image) {
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "ImageService{" +
                "id='" + id + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}