package com.example.piyush0.questionoftheday.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by piyush0 on 20/12/16.
 */

public class Challenge {
    private ArrayList<User> usersInGame;
    private ArrayList<Double> score;
    private ArrayList<Boolean> statuses;
    private Date date;
    private String topic;
    private Integer challenge_Id;
    private ArrayList<Question> questions;

    public Challenge(ArrayList<User> usersInGame,
                     ArrayList<Double> score, ArrayList<Boolean> pendingList, Date date, String topic, Integer challenge_Id, ArrayList<Question> questions) {
        this.usersInGame = usersInGame;
        this.score = score;
        this.statuses = pendingList;
        this.date = date;
        this.topic = topic;
        this.challenge_Id = challenge_Id;
        this.questions = questions;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<Double> getScore() {
        return score;
    }

    public void setScore(ArrayList<Double> score) {
        this.score = score;
    }

    public ArrayList<Boolean> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Boolean> statuses) {
        this.statuses = statuses;
    }

    public Integer getChallenge_Id() {
        return challenge_Id;
    }

    public void setChallenge_Id(Integer challenge_Id) {
        this.challenge_Id = challenge_Id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<User> getUsersInGame() {
        return usersInGame;
    }

    public void setUsersInGame(ArrayList<User> usersInGame) {
        this.usersInGame = usersInGame;
    }

    public ArrayList<Double> getMarks() {
        return score;
    }

    public void setMarks(ArrayList<Double> marks) {
        this.score = marks;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
