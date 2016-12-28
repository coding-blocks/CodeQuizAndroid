package com.example.piyush0.questionoftheday.dummy_utils;

import com.example.piyush0.questionoftheday.models.Challenge;
import com.example.piyush0.questionoftheday.models.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by piyush0 on 20/12/16.
 */

public class DummyChallenges {

    public static ArrayList<Challenge> getDummyChallenges() {

        ArrayList<Challenge> challenges = new ArrayList<>();

        ArrayList<Boolean> statuses = new ArrayList<>();
        for (int i = 0; i < DummyUsers.getUsers().size(); i++) {
            if (i % 2 == 0) {
                statuses.add(true);
            } else {
                statuses.add(false);
            }
        }

        ArrayList<Double> scores = new ArrayList<>();

        scores.add(11.2);
        scores.add(43.1);
        scores.add(5.3);
        scores.add(23.5);
        scores.add(16.4);
        scores.add(99.4);


        Challenge challenge1 = new Challenge(DummyUsers.getUsers(), scores, statuses, new Date(2016, 2, 3), "C++", 0);


        User user1 = new User("A", 23, "fns");
        User user2 = new User("B", 24, "fnsfdsf");

        ArrayList<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);


        challenges.add(challenge1);

        challenges.add(challenge1);

        challenges.add(challenge1);

        challenges.add(challenge1);

        challenges.add(challenge1);

        return challenges;
    }
}
