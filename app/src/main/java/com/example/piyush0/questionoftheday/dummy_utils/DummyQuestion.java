package com.example.piyush0.questionoftheday.dummy_utils;

import com.example.piyush0.questionoftheday.models.Option;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.models.Topic;

import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;

/**
 * Created by piyush0 on 05/12/16.
 */

public class DummyQuestion {

    public static Question getDummyQuestion() {

        RealmList<Option> options = new RealmList<>();


        options.add(new Option("This is a big text to check sizes", true));
        options.add(new Option("op2", false));
        options.add(new Option("op3", true));
        options.add(new Option("op4", false));


        RealmList<Topic> tags = new RealmList<>();
        tags.add(new Topic("Java"));
        tags.add(new Topic("C++"));

        Question question = new Question("What is abcd?",
                options, tags, "2016");

        return question;
    }

    public static ArrayList<Question> getDummyQuestions() {

        ArrayList<Question> questions = new ArrayList<>();
        questions.add(getDummyQuestion());
        RealmList<Option> options1 = new RealmList<>();

        options1.add(new Option("op1", true));
        options1.add(new Option("op2", false));
        options1.add(new Option("op3", false));
        options1.add(new Option("op4", true));

        RealmList<Topic> tags1 = new RealmList<>();
        tags1.add(new Topic("Java"));
        tags1.add(new Topic("C++"));


        RealmList<Option> options2 = new RealmList<>();

        options2.add(new Option("op5", true));
        options2.add(new Option("op6", true));
        options2.add(new Option("op7", true));
        options2.add(new Option("op8", false));
        RealmList<Topic> tags2 = new RealmList<>();
        tags2.add(new Topic("Java"));
        tags2.add(new Topic("Python"));


        Question question1 = new Question("What is abcde?",
                options1, tags1, "12/42/41");

        Question question2 = new Question("What is 2?",
                options2, tags2, "2/42/3");

        questions.add(question1);
        questions.add(question2);


        RealmList<Option> options3 = new RealmList<>();
        options3.add(new Option("opa", true));
        options3.add(new Option("opb", false));
        options3.add(new Option("opc", false));
        options3.add(new Option("opd", false));
        Question question3 = new Question("What is 3?", options3, new RealmList<Topic>(), "2/4/24");

        questions.add(question3);

        return questions;

    }
}
