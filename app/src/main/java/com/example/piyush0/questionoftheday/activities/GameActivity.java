package com.example.piyush0.questionoftheday.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.fragments.SolveQuestionFragment;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.CheckAnswer;
import com.example.piyush0.questionoftheday.utils.FontsOverride;
import com.example.piyush0.questionoftheday.utils.InitOptionsSelectedArray;
import com.example.piyush0.questionoftheday.utils.TimeUtil;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements SolveQuestionFragment.OnBooleanArrayPass {

    public static final String TAG = "GameActivity";

    private String selectedTopic;
    private Integer numOfQuestionsSelected;
    private ArrayList<String> usersChallenged;

    private ArrayList<Question> questions;
    private TextView tv_clock_minutes, tv_clock_seconds;
    private Button btn_next;

    private long timeForGame;

    private Handler handler;

    private int counter;
    private int numCorrect;

    private ArrayList<ArrayList<Integer>> optionsYouSelected;
    private ArrayList<Boolean> correctsAndIncorrects;

    private ArrayList<Boolean> optionsSelected;

    private boolean finishedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA,getWindow());


        optionsSelected = InitOptionsSelectedArray.init(optionsSelected);

        optionsYouSelected = new ArrayList<>();
        correctsAndIncorrects = new ArrayList<>();

        handler = new Handler();
        handler.post(runnable);

        getQuestions();
        getIntentExtras();
        initViews();
        setListenerOnButton();
    }

    private void initViews() {

        //TODO: Load the correct question using IDs.
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.activity_game_frag_container, SolveQuestionFragment.newInstance(0, false, "GameActivity")).
                commit();


        tv_clock_minutes = (TextView) findViewById(R.id.activity_game_clock_minutes);
        tv_clock_seconds = (TextView) findViewById(R.id.activity_game_clock_seconds);

        btn_next = (Button) findViewById(R.id.actvity_game_btn_next);
        btn_next.setText("Next");

    }

    private void getQuestions() {
        questions = DummyQuestion.getDummyQuestions();
        //TODO: Get Questions based on number of questions and topic.
    }

    private void setListenerOnButton() {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Integer> optionsYouSelectedInt = getOptionsYouSelectedInt(optionsSelected, questions.get(counter));
                optionsYouSelected.add(optionsYouSelectedInt);
                boolean isCorrectlySolved = CheckAnswer.isCorrect(optionsSelected, questions.get(counter));


                correctsAndIncorrects.add(isCorrectlySolved);

                optionsSelected = InitOptionsSelectedArray.init(optionsSelected);

                if (isCorrectlySolved) {
                    numCorrect++;
                }

                counter++;

                if (counter == questions.size() - 1) {
                    btn_next.setText("Submit");
                }

                if (counter == questions.size()) { /*Game ended*/
                    Log.d(TAG, "onClick: " + timeForGame);
                    Log.d(TAG, "onClick: " + numCorrect);


                    if (!finishedOnce) {
                        finishedOnce = true;
                        endGame();
                    }
                    /*TODO: Make API call. You have the following vars:
                    numCorrect, timeForGame*/

                } else {
                    loadNextQuestion();
                }
            }
        });
    }

    private void endGame() {
        int diff = questions.size() - correctsAndIncorrects.size();

        for (int i = 0; i < diff; i++) {
            correctsAndIncorrects.add(false);
            optionsYouSelected.add(new ArrayList<Integer>());
        }
        stopClock();


        Intent intent = new Intent(GameActivity.this, GameResultsActivity.class);
        intent.putExtra("timeForGame", timeForGame);
        intent.putExtra("optionsYouSelected", optionsYouSelected);
        intent.putExtra("correctsAndIncorrects", correctsAndIncorrects);

        clearLocalVars();

        startActivity(intent);

    }


    private ArrayList<Integer> getOptionsYouSelectedInt(ArrayList<Boolean> optionsSelectedBool, Question question) {

        ArrayList<Integer> retVal = new ArrayList<>();

        for (int i = 0; i < question.getOptions().size(); i++) {
            if (optionsSelectedBool.get(i)) {
                retVal.add(i);
            }
        }
        Log.d(TAG, "getOptionsYouSelectedInt: " + retVal);
        return retVal;

    }

    private void loadNextQuestion() {
        //TODO: Get next question based on IDs.
        getSupportFragmentManager().
                beginTransaction().replace(R.id.activity_game_frag_container, SolveQuestionFragment.newInstance(counter, false, "GameActivity")).
                commit();
    }


    private void clearLocalVars() {
        timeForGame = 0L;
        numCorrect = 0;
        counter = 0;
    }

    private void stopClock() {
        handler.removeCallbacks(runnable);
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        selectedTopic = intent.getStringExtra("selectedTopic");
        numOfQuestionsSelected = intent.getIntExtra("numOfQuestionsSelected", 0);
        usersChallenged = intent.getStringArrayListExtra("usersChallenged");
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            tv_clock_minutes.setText(TimeUtil.getMinutesAndSecond(timeForGame).get(0));
            tv_clock_seconds.setText(TimeUtil.getMinutesAndSecond(timeForGame).get(1));
            timeForGame = timeForGame + 1000;

            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Ending Game")
                .setMessage("Are you sure you want to exit the game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!finishedOnce) {
                            finishedOnce = true;
                            endGame();
                        }
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onStop() {
        if (!finishedOnce) {
            finishedOnce = true;
            endGame();
        }
        finish();

        super.onStop();
    }

    @Override
    public void onBooleanArrayPass(ArrayList<Boolean> optionsSelected) {
        this.optionsSelected = optionsSelected;
        Log.d(TAG, "onBooleanArrayPass: " + this.optionsSelected);
    }


}