package com.example.bakeryapplication.Models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String name, email, password, phone;
    private String image;
    String id ;
    String fcmtoken ;

    public String getFcmtoken() {
        return fcmtoken;
    }

    public void setFcmtoken(String fcmtoken) {
        this.fcmtoken = fcmtoken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserModel(String name, String email, String password, String phone) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public UserModel() {
    }
}
