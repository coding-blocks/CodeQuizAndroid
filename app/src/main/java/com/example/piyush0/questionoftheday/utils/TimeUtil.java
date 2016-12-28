package com.example.piyush0.questionoftheday.utils;

import java.util.ArrayList;

/**
 * Created by piyush0 on 22/12/16.
 */

public class TimeUtil {

    public static ArrayList<String> getMinutesAndSecond(long miliseconds){
        long minutes = (miliseconds / 1000) / 60;
        long seconds = (miliseconds / 1000) % 60;

        String minutesString = "";
        if (minutes < 10) {
            minutesString = "0" + String.valueOf(minutes) + ": ";
        } else {
            minutesString = String.valueOf(minutes) + ": ";
        }

        String secondsString = "";
        if (seconds < 10) {
            secondsString = "0" + String.valueOf(seconds);
        } else {
            secondsString = String.valueOf(seconds);
        }

        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(minutesString);
        retVal.add(secondsString);

        return retVal;

    }
}
