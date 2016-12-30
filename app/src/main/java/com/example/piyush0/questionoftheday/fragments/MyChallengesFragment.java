package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.api.ChallengeApi;
import com.example.piyush0.questionoftheday.dummy_utils.DummyChallenges;
import com.example.piyush0.questionoftheday.models.Challenge;
import com.example.piyush0.questionoftheday.models.ChallengeSmall;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyChallengesFragment extends Fragment {


    private View view;
    private RecyclerView rv_challenges;

    private ArrayList<ChallengeSmall> challengeSmalls;

    public MyChallengesFragment() {
        // Required empty public constructor
    }

    public static MyChallengesFragment newInstance() {
        return new MyChallengesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_my_challenges, container, false);
        fetchChallenges();

        return view;
    }


    private void fetchChallenges() {

        String url = getResources().getString(R.string.localhost_url) + "challenge/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build();
        ChallengeApi challengeApi = retrofit.create(ChallengeApi.class);

        challengeApi.getSmallChallenge().enqueue(new Callback<ArrayList<ChallengeSmall>>() {
            @Override
            public void onResponse(Call<ArrayList<ChallengeSmall>> call, Response<ArrayList<ChallengeSmall>> response) {
                challengeSmalls = response.body();
                initViews(view);
            }

            @Override
            public void onFailure(Call<ArrayList<ChallengeSmall>> call, Throwable t) {

            }
        });
    }

    private void initViews(View view) {
        rv_challenges = (RecyclerView) view.findViewById(R.id.fragment_my_challenges_list_challenges);
        rv_challenges.setAdapter(new ChallengeAdapter());
        rv_challenges.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private class ChallengeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_challengeName, tv_userChallenged, tv_date;

        ChallengeViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ChallengeAdapter extends RecyclerView.Adapter<ChallengeViewHolder> {

        @Override
        public ChallengeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView = li.inflate(R.layout.list_item_challenge, parent, false);

            ChallengeViewHolder challengeViewHolder = new ChallengeViewHolder(convertView);
            challengeViewHolder.tv_challengeName = (TextView) convertView.findViewById(R.id.list_item_challenge_tv_challenge_name);
            challengeViewHolder.tv_userChallenged = (TextView) convertView.findViewById(R.id.list_item_challenge_tv_users_in_game);
            challengeViewHolder.tv_date = (TextView) convertView.findViewById(R.id.list_item_challenge_tv_challenge_date);

            return challengeViewHolder;

        }

        @Override
        public void onBindViewHolder(final ChallengeViewHolder holder, int position) {
            holder.tv_challengeName.setText(challengeSmalls.get(position).toString());
            holder.tv_userChallenged.setText(challengeSmalls.get(position).usersChallenged());
            Date date = challengeSmalls.get(position).getDate();
            holder.tv_date.setText(date.getDate() + "/" + date.getMonth() + "/" + date.getYear());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().
                            getSupportFragmentManager().
                            beginTransaction().
                            replace(R.id.content_main,
                                    ChallengeDetailsFragment.newInstance(challengeSmalls
                                            .get(holder.getAdapterPosition()).getChallengeId())).
                            commit();
                }
            });
        }

        @Override
        public int getItemCount() {

            return challengeSmalls.size();

        }
    }
}
