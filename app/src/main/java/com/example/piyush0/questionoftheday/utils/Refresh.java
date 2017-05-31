package com.example.piyush0.questionoftheday.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
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

import java.io.IOException;

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
    public static final String TAG = "refresh";

    public static void refresh(SharedPreferences sharedPreferences, FragmentManager fragmentManager, Context context) {
        Realm realm = Realm.getDefaultInstance();
        Log.d(TAG, "refresh: " + realm.where(Question.class).findAll());
        boolean isDownloaded = sharedPreferences.getBoolean("isDownloaded", false);
        if (isDownloaded) {
            Log.d(TAG, "refresh: isDownloaded true");
            boolean isOpened = sharedPreferences.getBoolean("isOpened", false);

            if (isOpened) {
                Log.d(TAG, "refresh: is Opened true");
                int attempts = sharedPreferences.getInt("attempts", 0);
                boolean isCorrect = sharedPreferences.getBoolean("isCorrect", false);

                if (isCorrect) {
                    Log.d(TAG, "refresh: is Correct true");
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
                Log.d(TAG, "refresh: isOpened false");
                fragmentManager.beginTransaction()
                        .replace(R.id.content_main,
                                YouHaveANewQuesFragment.newInstance()).commit();
            }

        } else {
            Log.d(TAG, "refresh: is Download false");
            if (youHaveInternet(context)) {
                clearSharedPref(sharedPreferences);
                downloadAndSaveToDb(context, fragmentManager, sharedPreferences,true);

            } else {
                Toast.makeText(context, "Connect to the internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static boolean youHaveTheSameQuestion(Context context) {
        Question question = downloadSync(context);
        if (question != null) {
            Realm realm = Realm.getDefaultInstance();
            Question dbQues = realm.where(Question.class).equalTo("isToday", true).findFirst();
            if (dbQues.getId() == question.getId()) {
                return true;
            }
        }

        return false;

    }

    private static Question downloadSync(final Context context) {
        String url = context.getResources().getString(R.string.localhost_url) + "questions/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build();
        QuestionApi questionApi = retrofit.create(QuestionApi.class);
        final Question[] question = new Question[1];
        try {
            Response<Question> response = questionApi.getTodaysQuestion().execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void download(final Context context, final SharedPreferences sharedPreferences, final FragmentManager fragmentManager, final boolean refresh) {
        String url = context.getResources().getString(R.string.localhost_url) + "questions/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build();

        QuestionApi questionApi = retrofit.create(QuestionApi.class);
        final Question[] question = new Question[1];

        questionApi.getTodaysQuestion().enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {

                question[0] = response.body();

                question[0].setToday(true);
//                if(response.body() == null) {
//                  Toast.makeText(context,"Data not fetched",Toast.LENGTH_LONG).show();
//                    Log.d("Solved this thing","NULL");
//                } else {
//                    if (saveToDb) {
//                        saveQuestionToLocalDb(question[0]);
//                    }
//                }

                if (refresh) {
                    saveQuestionToLocalDb(question[0]);
                    sharedPreferences.edit().putBoolean("isDownloaded", true).apply();
                    refresh(sharedPreferences, fragmentManager, context);
                }else{
                    sharedPreferences.edit().putBoolean("isDownloaded", true).apply();
                }

            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {

                t.printStackTrace();
            }
        });

    }

    private static void downloadAndSaveToDb(Context context, FragmentManager fragmentManager, SharedPreferences sharedPreferences, Boolean refresh) {
        download(context,sharedPreferences,fragmentManager,refresh);
    }


    private static void saveQuestionToLocalDb(Question question) {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Question> yesterdayQuestions = realm.where(Question.class).equalTo("isToday", true).findAll();
        realm.beginTransaction();
        for (Question q : yesterdayQuestions) {
            q.setToday(false);
        }

        question.setToday(true);
        Log.d(TAG, "saveQuestionToLocalDb: " + question);
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
        editor.putBoolean("tipDownloaded", false);
        editor.apply();
    }


    public static void job(SharedPreferences sharedPreferences, Context context) {

        if (youHaveAQuestion()) {

            if (youHaveInternet(context)) {
                if (youHaveTheSameQuestion(context)) {
                    sharedPreferences.edit().putBoolean("isDownloaded", true).apply();
                } else {
                    clearSharedPref(sharedPreferences);
                    downloadAndSaveToDb(context,null,sharedPreferences,false);

                }
            } else {
                sharedPreferences.edit().putBoolean("isDownloaded", true).apply();
            }

        } else {
            if (youHaveInternet(context)) {
                clearSharedPref(sharedPreferences);
                downloadAndSaveToDb(context,null,sharedPreferences,false);
            } else {
                sharedPreferences.edit().putBoolean("isDownloaded", false).apply();
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
