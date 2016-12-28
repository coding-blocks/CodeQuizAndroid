package com.example.piyush0.questionoftheday.api;

import com.example.piyush0.questionoftheday.models.Tip;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by piyush0 on 28/12/16.
 */

public interface TipApi {
    @GET("todayTip")
    Call<Tip> getTodaysTip();
}
