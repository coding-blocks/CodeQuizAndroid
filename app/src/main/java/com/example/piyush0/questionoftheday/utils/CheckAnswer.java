package com.example.piyush0.questionoftheday.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.models.Question;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by piyush0 on 08/12/16.
 */

public class CheckAnswer {


    public static boolean isCorrect(ArrayList<Boolean> optionsSelected, Question question) {


        for(int i = 0 ; i< question.getOptions().size(); i++) {
            if(optionsSelected.get(i) != question.getOptions().get(i).isCorrect()) {
                return false;
            }
        }
        return true;

    }


}
