package com.example.piyush0.questionoftheday.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.dummy_utils.DummyUsers;
import com.example.piyush0.questionoftheday.models.User;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {
    private User me;

    private ImageView iv_userPic;
    private TextView tv_name, tv_email, tv_rating;
    private SimpleRatingBar ratingBar;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fetchMe();
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        init(view);
        return view;
    }

    private void fetchMe() {
        //TODO:
        me = DummyUsers.getUsers().get(0);
    }

    private void init(View view) {
        iv_userPic = (ImageView) view.findViewById(R.id.activity_profile_picture);
        tv_name = (TextView) view.findViewById(R.id.activity_profile_name);
        tv_email = (TextView) view.findViewById(R.id.activity_profile_email);
        tv_rating = (TextView) view.findViewById(R.id.activity_profile_rating);
        ratingBar = (SimpleRatingBar) view.findViewById(R.id.activity_profile_rating_bar);
        ratingBar.setIndicator(true); /*This doesn't let the user change his rating*/
        initRatingBar();

        setDetails();
    }

    private void setDetails() {
        tv_email.setText(me.getEmail());
        tv_rating.setText(me.getScore().toString());
        tv_name.setText(me.getName());
        ratingBar.setRating(calculateStars(Float.valueOf(tv_rating.getText().toString())));
        //TODO: set pic.
    }

    private float calculateStars(float rating) {
        float perc = rating / 100;
        return perc * 5;
    }

    private void initRatingBar() {
        ratingBar.setStarSize(80);
        ratingBar.setNumberOfStars(5);
        ratingBar.setStepSize(0.1f);
        ratingBar.setBorderColor(getResources().getColor(R.color.darkRed));
        ratingBar.setFillColor(getResources().getColor(R.color.colorPrimary));
        ratingBar.setStarCornerRadius(10);
    }

}
