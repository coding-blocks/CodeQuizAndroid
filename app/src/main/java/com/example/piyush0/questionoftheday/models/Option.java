package com.example.piyush0.questionoftheday.models;

import io.realm.RealmObject;

/**
 * Created by piyush0 on 05/12/16.
 */

public class Option extends RealmObject {

    private String answer;
    private boolean correct;

    public Option(String answer, boolean correct) {
        this.answer = answer;
        this.correct = correct;
    }

    public Option() {
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
