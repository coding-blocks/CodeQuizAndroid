package com.example.piyush0.questionoftheday.api;

import com.example.piyush0.questionoftheday.models.Question;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by piyush0 on 27/12/16.
 */

public interface QuestionApi {

    @GET("archive")
    Call<ArrayList<Question>> listQuestions();

    @POST("archive")
    Call<ArrayList<Question>> listQuestionWithFilter(@Body Request request);

    @GET("today")
    Call<Question> getTodaysQuestion();

    class Request{
        ArrayList<String> filters;
        String sortBy;

        public ArrayList<String> getFilters() {
            return filters;
        }

        public void setFilters(ArrayList<String> filters) {
            this.filters = filters;
        }

        public String getSortBy() {
            return sortBy;
        }

        public void setSortBy(String sortBy) {
            this.sortBy = sortBy;
        }
    }
}
