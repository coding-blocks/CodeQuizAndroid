package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.activities.MainActivity;
import com.example.piyush0.questionoftheday.api.TipApi;
import com.example.piyush0.questionoftheday.models.Tip;

import org.w3c.dom.Text;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TipFragment extends Fragment {

    private Tip tip;
    private TextView tv_tip;
    private SharedPreferences sharedPreferences;

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


        if (sharedPreferences.getBoolean("tipDownloaded", false)) {
            getTipFromDb();
        } else {
            fetchTip();
        }
        tv_tip = (TextView) view.findViewById(R.id.fragment_tip_tv_tip);

        return view;
    }

    private void fetchTip() {
        String url = getResources().getString(R.string.localhost_url) + "tips/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build();
        TipApi tipApi = retrofit.create(TipApi.class);

        tipApi.getTodaysTip().enqueue(new Callback<Tip>() {
            @Override
            public void onResponse(Call<Tip> call, Response<Tip> response) {
                tip = response.body();
                tv_tip.setText(tip.getTipName());

                sharedPreferences = getContext().
                        getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("tipDownloaded", true);
                editor.commit();
                saveTipToDb(tip);
            }

            @Override
            public void onFailure(Call<Tip> call, Throwable t) {

            }
        });
    }

    private void getTipFromDb() {
        Realm realm = Realm.getDefaultInstance();
        tip = realm.where(Tip.class).findFirst();
        tv_tip.setText(tip.getTipName());
    }

    private void saveTipToDb(Tip tip) {
        deleteOlderTip();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(tip);
        realm.commitTransaction();
    }

    private void deleteOlderTip() {
        Realm realm = Realm.getDefaultInstance();

        final RealmResults<Tip> previousTips = realm.where(Tip.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                previousTips.deleteAllFromRealm();
            }
        });

    }

}
