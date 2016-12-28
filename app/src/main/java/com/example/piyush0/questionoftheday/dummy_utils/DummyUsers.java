package com.example.piyush0.questionoftheday.dummy_utils;

import android.widget.TextView;

import com.example.piyush0.questionoftheday.models.User;

import java.util.ArrayList;

/**
 * Created by piyush0 on 05/12/16.
 */

public class DummyUsers {

    //This is used by dummy challenge only.

    public static ArrayList<User> getUsers(){
        ArrayList<User> users = new ArrayList<>();

        users.add(new User("User1",50,"123@gmail.com"));
        users.add(new User("User2",30,"234@gmail.com"));
        users.add(new User("User3",70,"453@gmail.com"));
        users.add(new User("User4",100,"dfsv@gmail.com"));
        users.add(new User("User5",35,"asdfv@gmail.com"));
        users.add(new User("User6",234,"asdfv@gmail.com"));

        return users;
    }
}
