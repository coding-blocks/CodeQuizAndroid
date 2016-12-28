package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class YouHaveANewQuesFragment extends Fragment {

    private Button btn_letsSolve;

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    public YouHaveANewQuesFragment() {
        // Required empty public constructor
    }


    public static YouHaveANewQuesFragment newInstance() {
        return new YouHaveANewQuesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initSharedPrefs();
        View view = inflater.inflate(R.layout.fragment_you_have_a_new_ques, null);
        initViews(view);
        setClickedListenerOnBtn();
        return view;
    }

    private void initSharedPrefs() {

        sharedPrefs = getActivity().getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
    }

    private void setClickedListenerOnBtn() {
        btn_letsSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("isOpened", true);
                editor.putLong("timeForTodayQues", 0);
                editor.commit();

                getFragmentManager().beginTransaction().
                        replace(R.id.content_main, SolveTodayQuestionFragment.newInstance()).commit();
            }
        });
    }

    private void initViews(View view) {
        btn_letsSolve = (Button) view.findViewById(R.id.fragment_today_btn_letsSolve);
    }
}
