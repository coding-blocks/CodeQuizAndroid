package com.example.piyush0.questionoftheday.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.api.QuestionApi;
import com.example.piyush0.questionoftheday.fragments.OOPSFragment;
import com.example.piyush0.questionoftheday.fragments.SolveTodayQuestionFragment;
import com.example.piyush0.questionoftheday.fragments.TipFragment;
import com.example.piyush0.questionoftheday.fragments.YouHaveANewQuesFragment;
import com.example.piyush0.questionoftheday.models.Question;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by piyush0 on 07/12/16.
 */

public class Refresh {


    public static void refresh(SharedPreferences sharedPreferences, FragmentManager fragmentManager, Context context) {


        boolean isDownloaded = sharedPreferences.getBoolean("isDownloaded", false);
        if (isDownloaded) {


            boolean isOpened = sharedPreferences.getBoolean("isOpened", false);

            if (isOpened) {

                int attempts = sharedPreferences.getInt("attempts", 0);
                boolean isCorrect = sharedPreferences.getBoolean("isCorrect", false);

                if (isCorrect) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_main,
                                    TipFragment.newInstance()).commit();
                } else {
                    if (attempts < 3) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_main,
                                        SolveTodayQuestionFragment.newInstance()).commit();
                    } else {
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_main,
                                        OOPSFragment.newInstance()).commit();
                    }
                }
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_main,
                                YouHaveANewQuesFragment.newInstance()).commit();
            }

        } else {
            if (youHaveInternet(context)) {
                clearSharedPref(sharedPreferences);
                downloadAndSaveToDb(context);
                sharedPreferences.edit().putBoolean("isDownloaded", true).commit();
                refresh(sharedPreferences, fragmentManager, context);
            } else {
                Toast.makeText(context, "Connect to the internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static boolean youHaveTheSameQuestion(Context context) {
        Question question = download(context, false);
        if (question != null) {
            Realm realm = Realm.getDefaultInstance();
            Question dbQues = realm.where(Question.class).equalTo("isToday", true).findFirst();
            if (dbQues.getId() == question.getId()) {
                return true;
            }
        }

        return false;

    }

    private static Question download(Context context, final Boolean saveToDb) {
        String url = context.getResources().getString(R.string.localhost_url) + "questions/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build();

        QuestionApi questionApi = retrofit.create(QuestionApi.class);
        final Question[] question = new Question[1];

        questionApi.getTodaysQuestion().enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {

                question[0] = response.body();
                question[0].setToday(true);
                if (saveToDb) {
                    saveQuestionToLocalDb(question[0]);
                }

            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {

                t.printStackTrace();
            }
        });

        return question[0];
    }

    private static void downloadAndSaveToDb(Context context) {
        download(context, true);
    }


    private static void saveQuestionToLocalDb(Question question) {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Question> yesterdayQuestions = realm.where(Question.class).equalTo("isToday", true).findAll();

        for (Question q : yesterdayQuestions) {
            q.setToday(false);
        }

        realm.beginTransaction();
        question.setToday(true);
        realm.copyToRealm(question);
        realm.commitTransaction();

    }

    private static void clearSharedPref(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Long zero = 0L;
        editor.putLong("timeForTodayQues", zero);
        editor.putBoolean("isOpened", false);
        editor.putInt("attempts", 0);
        editor.putBoolean("isCorrect", false);
        editor.commit();
    }


    public static void job(SharedPreferences sharedPreferences, Context context) {

        if (youHaveAQuestion()) {

            if (youHaveInternet(context)) {
                if (youHaveTheSameQuestion(context)) {
                    sharedPreferences.edit().putBoolean("isDownloaded", true).commit();
                } else {
                    clearSharedPref(sharedPreferences);
                    downloadAndSaveToDb(context);
                    sharedPreferences.edit().putBoolean("isDownloaded", true).commit();
                }
            } else {
                sharedPreferences.edit().putBoolean("isDownloaded", true).commit();
            }

        } else {
            if (youHaveInternet(context)) {
                clearSharedPref(sharedPreferences);
                downloadAndSaveToDb(context);
                sharedPreferences.edit().putBoolean("isDownloaded", true).commit();
            } else {
                sharedPreferences.edit().putBoolean("isDownloaded", false).commit();
            }
        }


    }

    private static boolean youHaveAQuestion() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Question> questions = realm.where(Question.class).equalTo("isToday", true).findAll();
        return questions.size() > 0;
    }


    public static boolean youHaveInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }
}
