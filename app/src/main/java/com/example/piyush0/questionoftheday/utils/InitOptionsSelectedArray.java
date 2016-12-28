package com.example.piyush0.questionoftheday.utils;

import java.util.ArrayList;

/**
 * Created by piyush0 on 16/12/16.
 */

public class InitOptionsSelectedArray{
    private static final int MAX_NUM_OF_OPTIONS = 20;

    public static ArrayList<Boolean> init(ArrayList<Boolean> optionsSelected) {
        optionsSelected = new ArrayList<>();
        for(int i = 0 ; i<MAX_NUM_OF_OPTIONS; i++) {
            optionsSelected.add(false);
        }

        return optionsSelected;
    }
}
