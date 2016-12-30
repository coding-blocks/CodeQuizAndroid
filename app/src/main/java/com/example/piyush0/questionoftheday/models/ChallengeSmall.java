package com.example.piyush0.questionoftheday.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by piyush0 on 30/12/16.
 */

public class ChallengeSmall {

    private Integer challengeId;
    private String topic;
    private Date date;
    private ArrayList<String> userNames;

    public Integer getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(ArrayList<String> userNames) {
        this.userNames = userNames;
    }

    public String usersChallenged() {
        String rv = "";

        if (userNames.size() > 3) {

            for (int i = 0; i < 2; i++) {
                rv += userNames.get(i) + ", ";
            }

            rv += userNames.get(2) + " and " + (userNames.size() - 3) + " others.";
            return rv;

        } else {
            for (int i = 0; i < userNames.size() - 1; i++) {
                rv += userNames.get(i) + ", ";
            }

            rv += userNames.get(userNames.size() - 1) + ".";
            return rv;
        }
    }

    @Override
    public String toString() {
        String rv = topic + " Quiz";

        return rv;
    }
}
