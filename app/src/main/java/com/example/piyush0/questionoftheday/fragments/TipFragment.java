package com.example.piyush0.questionoftheday.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.api.TipApi;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TipFragment extends Fragment {
    public static final String TAG = "TipFrag";
    private String tip;
    private TextView tv_tip;

    public TipFragment() {
        // Required empty public constructor
    }

    public static TipFragment newInstance() {
        return new TipFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tip, container, false);
        fetchTip();
        tv_tip = (TextView) view.findViewById(R.id.fragment_tip_tv_tip);

        return view;
    }

    private void fetchTip() {
        String url = getResources().getString(R.string.localhost_url) + "tips/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build();
        TipApi tipApi = retrofit.create(TipApi.class);

        tipApi.getTodaysTip().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                tip = response.body();
                tv_tip.setText(tip);
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
