package com.example.piyush0.questionoftheday.api;

import com.example.piyush0.questionoftheday.models.Challenge;
import com.example.piyush0.questionoftheday.models.ChallengeSmall;
import com.example.piyush0.questionoftheday.models.Question;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by piyush0 on 30/12/16.
 */

public interface ChallengeApi {

    @GET("me")
    Call<ArrayList<ChallengeSmall>> getSmallChallenge();

    @GET("{id}/result")
    Call<Challenge> getChallenge(@Path("id") int id);

    @POST("create")
    Call<Integer> getChallengeId(@Body ChallengeCreator challengeCreator);

    @GET("{id}")
    Call<ArrayList<Question>> getQuestions(@Path("id") int id);

    @POST("submit/{id}")
    Call<Boolean> submit(@Body Submission submission, @Path("id") int id);

    class Submission{

        Integer numCorrect;
        Long timeTaken;

        public Submission(Integer numCorrect, Long timeTaken) {

            this.numCorrect = numCorrect;
            this.timeTaken = timeTaken;
        }
    }

    class ChallengeCreator{
        ArrayList<Integer> users;
        Integer numQues;
        String topic;

        public ChallengeCreator(ArrayList<Integer> users, Integer numQues, String topic) {
            this.users = users;
            this.numQues = numQues;
            this.topic = topic;
        }
    }
}
