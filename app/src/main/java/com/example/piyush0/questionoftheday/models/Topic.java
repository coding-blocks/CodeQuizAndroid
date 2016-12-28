package com.example.piyush0.questionoftheday.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by piyush0 on 27/12/16.
 */

public class Topic extends RealmObject {
    private String name;

    public Topic() {
    }

    public Topic(String name) {
        this.name = name;
    }
}
