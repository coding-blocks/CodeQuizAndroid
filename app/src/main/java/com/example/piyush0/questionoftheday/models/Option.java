package com.example.piyush0.questionoftheday.models;

import android.util.Log;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by piyush0 on 05/12/16.
 */

public class Option extends RealmObject {
    public static final String TAG = "Options";
    private String answer;
    private boolean correct;
    private boolean isSelected;

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        Log.d(TAG, "setSelected: " + this.isSelected());
    }

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
