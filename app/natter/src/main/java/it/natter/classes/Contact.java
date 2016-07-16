package it.natter.classes;

import android.graphics.Bitmap;

import it.natter.utility.Code;

/**
 * Created by francesco on 22/03/14.
 */
public class Contact implements Comparable<Contact>{

    private String id_natter;
    private String id_phone;
    private String name;
    private String surname;
    private String email;
    private String number;
    private Bitmap photo;
    private boolean hasPhoto = false;
    private boolean isErasable;

    public Contact(){
        this.id_natter = "";
    }

    public Contact(String id_natter,String id_phone,String name,String surname,String email,String number,Bitmap photo,boolean flag){
        this.id_natter = id_natter;
        this.id_phone = id_phone;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.number = number;
        this.photo = photo;
        this.isErasable = flag;
    }

    public String getId_natter() {
        return id_natter;
    }

    public void setId_natter(String id_natter) {
        this.id_natter = id_natter;
    }

    public String getId_phone() {
        return id_phone;
    }

    public void setId_phone(String id_phone) {
        this.id_phone = id_phone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo){
        if(photo==null){
            this.setHasPhoto(false);
        }
        else {
            this.setHasPhoto(true);
            this.photo = photo;
        }
    }

    public boolean isErasable() {
        return isErasable;
    }

    public void setErasable(boolean isErasable) {
        this.isErasable = isErasable;
    }

    public boolean hasPhoto() {
        return hasPhoto;
    }

    public void setHasPhoto(boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }

    public String getAllName(){
        return this.name+" "+this.surname;
    }

    public int compareTo(Contact c){
        return this.name.compareTo(c.getName());
    }

    public String[] stringArray(){
        String[] data = new String[7];
        data[0] = this.id_natter;
        data[1] = this.id_phone;
        data[2] = this.email;
        data[3] = this.name;
        data[4] = this.surname;
        data[5] = this.number;

        if(this.isErasable) {
            data[6] = "1";
        }
        else{
            data[6] = "0";
        }

        return data;
    }

    @Override
    public String toString(){
        String photo_value;
        if(this.photo == null){
            photo_value = Code.NOT_STATED;
        }
        else{
            photo_value = "#FILE#";
        }

        return "Contact{" +
                "id_natter='" + id_natter + '\'' +
                ", id_phone='" + id_phone + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", number='" + number + '\'' +
                ", hasPhoto=" + hasPhoto +
                ", photo='" + photo_value + '\'' +
                ", isErasable=" + isErasable +
                '}';
    }
}
