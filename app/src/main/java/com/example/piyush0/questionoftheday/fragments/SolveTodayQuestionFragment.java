package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.services.TimeCountingService;
import com.example.piyush0.questionoftheday.activities.MainActivity;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.CheckAnswer;
import com.example.piyush0.questionoftheday.utils.InitOptionsSelectedArray;
import com.example.piyush0.questionoftheday.utils.Refresh;
import com.example.piyush0.questionoftheday.utils.TimeUtil;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class SolveTodayQuestionFragment extends Fragment implements SolveQuestionFragment.OnBooleanArrayPass {

    public static final String SHARED_PREF_NAME = "TodaySolved";
    public static final String TAG = "SolveToday";

    private Context context;

    private TextView tv_attemptsRemaining, tv_clock_seconds, tv_clock_minutes;
    private Button btn_submit;

    private Handler handler; /*This is being used to calculate the time*/

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FragmentManager fragmentManager;

    private Question todaysQuestion;

    private boolean isCorrectlySolved;
    private Long timeTaken;
    private int attempts;
    private ArrayList<Boolean> optionsSelected;


    public SolveTodayQuestionFragment() {

    }

    public static SolveTodayQuestionFragment newInstance() {

        return new SolveTodayQuestionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        optionsSelected = InitOptionsSelectedArray.init(optionsSelected);
        getTodaysQuestion();
        initContext();
        View view = inflater.inflate(R.layout.fragment_solve_today_question, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initViews(view);
        setClickListenerOnBtn();
        return view;
    }

    private void setClickListenerOnBtn() {

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attempts = sharedPreferences.getInt("attempts", 0);
                attempts++;
                editor.putInt("attempts", attempts);

                tv_attemptsRemaining.setText(String.valueOf(3 - attempts));

                isCorrectlySolved = CheckAnswer.isCorrect(optionsSelected, todaysQuestion);
                editor.putBoolean("isCorrect", isCorrectlySolved);
                editor.commit();

                Refresh.refresh(sharedPreferences, fragmentManager, getContext());
            }
        });
    }

    @Override
    public void onBooleanArrayPass(ArrayList<Boolean> optionsSelected) {

        this.optionsSelected = optionsSelected;
    }


    private void initContext() {

        context = getActivity().getBaseContext();
    }

    @Override
    public void onResume() {

        super.onResume();
        initSharedPrefs();
        stopTimeCountingService();
        initClock();
    }

    private void initClock() {

        handler = new Handler();
        handler.post(runnable);
    }

    private void initSharedPrefs() {

        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Long zero = 0L;
        timeTaken = sharedPreferences.getLong("timeForTodayQues", zero);
    }

    private void stopTimeCountingService() {

        Intent intent = new Intent(getContext(), TimeCountingService.class);
        context.stopService(intent);
    }

    private void initViews(View view) {

        tv_clock_minutes = (TextView) view.findViewById(R.id.fragment_solve_today_question_minute);
        tv_clock_seconds = (TextView) view.findViewById(R.id.fragment_solve_today_question_second);
        tv_attemptsRemaining = (TextView) view.findViewById(R.id.fragment_solve_today_question_attempts);
        btn_submit = (Button) view.findViewById(R.id.fragment_solve_today_btn_sumbit);

        initSharedPrefsOnCreate();


    }

    private void getTodaysQuestion() {
        Realm realm = Realm.getDefaultInstance();
        Log.d(TAG, "getTodaysQuestion: " + realm.where(Question.class).findAll());
        todaysQuestion = realm.where(Question.class).equalTo("isToday", true).findFirst();

        getChildFragmentManager().
                beginTransaction().
                replace(R.id.fragment_solve_today_frag_container,
                        SolveQuestionFragment.newInstance(todaysQuestion.getId(), false, "SolveTodayQuestionFragment")).
                commit();
    }

    private void initSharedPrefsOnCreate() {

        sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        attempts = sharedPreferences.getInt("attempts", 0);
        tv_attemptsRemaining.setText(String.valueOf(3 - attempts));
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            tv_clock_minutes.setText(TimeUtil.getMinutesAndSecond(timeTaken).get(0));
            tv_clock_seconds.setText(TimeUtil.getMinutesAndSecond(timeTaken).get(1));
            timeTaken = timeTaken + 1000;
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onPause() {

        saveTimeTillNow();
        stopClock();
        startTimeCountingService();
        super.onPause();
    }

    private void saveTimeTillNow() {

        editor.putLong("timeForTodayQues", timeTaken);
        editor.commit();
    }

    private void stopClock() {

        handler.removeCallbacks(runnable);
    }

    private void startTimeCountingService() {

        Intent intent = new Intent(getContext(), TimeCountingService.class);
        intent.putExtra("timeForTodayQues", timeTaken);
        context.startService(intent);
    }


}
