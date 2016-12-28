package com.example.piyush0.questionoftheday.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by piyush0 on 05/12/16.
 */

public class Question extends RealmObject{

    private String question;
    private RealmList<Option> options;
    private RealmList<Topic> tags;
    private Integer id;
    private String date_added;
    private Boolean isToday;

    public Question(String question, RealmList<Option> options, RealmList<Topic> tags, String date) {
        this.question = question;
        this.options = options;
        this.date_added = date;
        this.tags = tags;
    }

    public Boolean getToday() {
        return isToday;
    }

    public void setToday(Boolean today) {
        isToday = today;
    }

    public RealmList<Topic> getTags() {
        return tags;
    }

    public void setTags(RealmList<Topic> tags) {
        this.tags = tags;
    }

    public Question() {
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public RealmList<Option> getOptions() {
        return options;
    }

    public void setOptions(RealmList<Option> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return this.getQuestion() + " : " + this.isToday;
    }
}
