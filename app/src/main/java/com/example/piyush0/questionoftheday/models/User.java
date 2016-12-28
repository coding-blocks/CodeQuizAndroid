package com.example.piyush0.questionoftheday.models;

import com.example.piyush0.questionoftheday.utils.MD5Gravatar;

import java.util.ArrayList;

/**
 * Created by piyush0 on 05/12/16.
 */

public class User {

    private String name;
    private String image_url;
    private String email;
    private Integer score;
    private Integer unique_id;

    public User(String name, Integer score, String email) {

        this.name = name;
        this.score = score;
        this.email = email;
        this.gravatarSupport();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.gravatarSupport();
    }

    public void gravatarSupport(){

        String hash = MD5Gravatar.md5Hex(email);
        image_url = "https://www.gravatar.com/avatar/" + hash;

    }


}
