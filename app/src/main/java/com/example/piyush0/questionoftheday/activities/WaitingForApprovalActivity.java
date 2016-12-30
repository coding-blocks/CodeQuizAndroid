package com.example.piyush0.questionoftheday.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.api.ChallengeApi;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.FontsOverride;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WaitingForApprovalActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_lets_go;
    private Button btn_temp;
    private LinearLayout layout_loading, layout_loading_done;

    private String selectedTopic;
    private Integer numOfQuestionsSelected;
    private ArrayList<Integer> usersChallenged;

    private ArrayList<String> questionArrayJson;

    public static final String TAG = "WaitingForAppAct";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_approval);

        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA,getWindow());

        initViews();
        getIntentExtras();
        btn_lets_go.setOnClickListener(this);

        createChallenge();
    }

    private void createChallenge() {
        String url = getResources().getString(R.string.localhost_url) + "challenge/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build();
        ChallengeApi challengeApi = retrofit.create(ChallengeApi.class);
        ChallengeApi.ChallengeCreator challengeCreator = new ChallengeApi.ChallengeCreator(usersChallenged,numOfQuestionsSelected,selectedTopic);

        challengeApi.getChallengeId(challengeCreator).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer cId = response.body();
                getQuestions(cId);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void getQuestions(Integer id) {
        String url = getResources().getString(R.string.localhost_url) + "challenge/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build();
        ChallengeApi challengeApi = retrofit.create(ChallengeApi.class);

        challengeApi.getQuestions(id).enqueue(new Callback<ArrayList<Question>>() {
            @Override
            public void onResponse(Call<ArrayList<Question>> call, Response<ArrayList<Question>> response) {
                ArrayList<Question> questions = response.body();
                questionArrayJson = getStringsFromQuestions(questions);
                loadingDone();
            }

            @Override
            public void onFailure(Call<ArrayList<Question>> call, Throwable t) {

            }
        });
    }

    private ArrayList<String> getStringsFromQuestions(ArrayList<Question> questions){
        ArrayList<String> retVal = new ArrayList<>();
        Gson gson = new Gson();
        for(int i =0; i<questions.size() ; i++) {
            retVal.add(gson.toJson(questions.get(i)));
        }

        return retVal;
    }

    private void loadingDone() {
        btn_temp.setVisibility(View.GONE);
        layout_loading.setVisibility(View.GONE);
        layout_loading_done.setVisibility(View.VISIBLE);
        btn_lets_go.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        btn_lets_go = (Button) findViewById(R.id.activity_waiting_for_approval_btn_letsGo);
        layout_loading = (LinearLayout) findViewById(R.id.activity_waiting_for_approval_waiting_layout);
        layout_loading_done = (LinearLayout) findViewById(R.id.activity_waiting_for_approval_loading_done_layout);
        layout_loading_done.setVisibility(View.GONE);
        btn_lets_go.setVisibility(View.GONE);

        btn_temp = (Button) findViewById(R.id.btn_temp);
        btn_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDone();
            }
        });
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        selectedTopic = intent.getStringExtra("selectedTopic");
        numOfQuestionsSelected = intent.getIntExtra("numOfQuestionsSelected", 0);
        usersChallenged = intent.getIntegerArrayListExtra("usersChallenged");
    }

    @Override
    public void onClick(View v) {
        sendIntent();
    }


    private void sendIntent() {
        Intent intent = new Intent(WaitingForApprovalActivity.this, GameActivity.class);
        intent.putExtra("selectedTopic", selectedTopic);
        intent.putExtra("numOfQuestionsSelected", numOfQuestionsSelected);
        intent.putExtra("usersChallenged", usersChallenged);
        intent.putExtra("questionArrayJson", questionArrayJson);
        startActivity(intent);
        finish();
    }
}
