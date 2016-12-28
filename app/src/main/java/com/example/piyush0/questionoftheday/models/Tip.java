package com.example.piyush0.questionoftheday.models;


import io.realm.RealmObject;

/**
 * Created by piyush0 on 28/12/16.
 */

public class Tip extends RealmObject {
    String tipName;

    public String getTipName() {
        return tipName;
    }

    public void setTipName(String tipName) {
        this.tipName = tipName;
    }
}
