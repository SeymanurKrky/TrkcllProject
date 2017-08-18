package com.androidtutorialshub.loginregister.model;

import java.io.Serializable;

/**
 * Created by lalit on 9/12/2016.
 */
public class User implements Serializable {

    private String name;
    private String email;
    private String surname;
    private String mobile;
    private String image;

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


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public User(String name, String email, String surname, String mobile) {
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.mobile = mobile;
    }

    public User() {
    }
}
