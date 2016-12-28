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
import com.example.piyush0.questionoftheday.dummy_utils.DummyChallenges;
import com.example.piyush0.questionoftheday.models.Challenge;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyChallengesFragment extends Fragment {

    public static final String TAG = "MyChalfrag";

    private RecyclerView rv_challenges;

    private Context context;

    private ArrayList<Challenge> challenges;

    public MyChallengesFragment() {
        // Required empty public constructor
    }

    public static MyChallengesFragment newInstance() {
        return new MyChallengesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_challenges, container, false);
        fetchChallenges();
        initContext();
        initViews(view);
        return view;
    }

    private void initContext() {
        this.context = getContext();
    }

    private void fetchChallenges() {
        //TODO: Fetch challenges
        challenges = DummyChallenges.getDummyChallenges();
        Log.d(TAG, "fetchChallenges: " + challenges);
    }

    private void initViews(View view) {
        rv_challenges = (RecyclerView) view.findViewById(R.id.fragment_my_challenges_list_challenges);
        rv_challenges.setAdapter(new ChallengeAdapter());
        rv_challenges.setLayoutManager(new LinearLayoutManager(context));
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
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView = li.inflate(R.layout.list_item_challenge, parent, false);

            ChallengeViewHolder challengeViewHolder = new ChallengeViewHolder(convertView);
            challengeViewHolder.tv_challengeName = (TextView) convertView.findViewById(R.id.list_item_challenge_tv_challenge_name);
            challengeViewHolder.tv_userChallenged = (TextView) convertView.findViewById(R.id.list_item_challenge_tv_users_in_game);
            challengeViewHolder.tv_date = (TextView) convertView.findViewById(R.id.list_item_challenge_tv_challenge_date);

            return challengeViewHolder;

        }

        @Override
        public void onBindViewHolder(ChallengeViewHolder holder, final int position) {
            holder.tv_challengeName.setText(challenges.get(position).toString());
            holder.tv_userChallenged.setText(challenges.get(position).usersChallenged());
            Date date = challenges.get(position).getDate();
            holder.tv_date.setText(date.getDate() + "/" + date.getMonth() + "/" + date.getYear());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().
                            getSupportFragmentManager().
                            beginTransaction().
                            replace(R.id.content_main, ChallengeDetailsFragment.newInstance(0)).
                            commit();

                    //TODO: Send correct id in newInstance parameters.
                }
            });
        }

        @Override
        public int getItemCount() {

            return challenges.size();

        }
    }
}
