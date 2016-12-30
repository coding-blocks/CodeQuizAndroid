package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.api.ChallengeApi;
import com.example.piyush0.questionoftheday.dummy_utils.DummyChallenges;
import com.example.piyush0.questionoftheday.models.Challenge;
import com.example.piyush0.questionoftheday.models.User;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeDetailsFragment extends Fragment {

    private Challenge challenge;

    private TableView tableView;
    private View view;

    public ChallengeDetailsFragment() {
    }

    public static ChallengeDetailsFragment newInstance(int challengeId) {
        Bundle args = new Bundle();
        args.putInt("challengeId", challengeId);
        ChallengeDetailsFragment challengeDetailsFragment = new ChallengeDetailsFragment();
        challengeDetailsFragment.setArguments(args);
        return challengeDetailsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_challenge_details, container, false);
        fetchChallenge();

        return view;
    }

    private void initViews(View view) {
        tableView = (TableView) view.findViewById(R.id.fragment_challenge_details_tableView);
        TableColumnWeightModel columnModel = new TableColumnWeightModel(5);
        columnModel.setColumnWeight(0, 2);
        columnModel.setColumnWeight(1, 1);
        columnModel.setColumnWeight(2, 4);
        columnModel.setColumnWeight(3, 3);
        columnModel.setColumnWeight(4, 2);
        tableView.setColumnModel(columnModel);
        tableView.setHeaderAdapter(new HeaderAdapter(getContext()));

        /*I dont understand the code written below. Its was suggested as a hack on github as Header Adapter was not performing well*/

        ViewTreeObserver vto = tableView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tableView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                tableView.setHeaderAdapter(new HeaderAdapter(getContext()));
            }
        });

        tableView.setDataAdapter(new TableAdapter(getContext(), challenge.getUsersInGame()));

    }

    private void fetchChallenge() {
        int challengeId = getArguments().getInt("challengeId", 0);

        String url = getResources().getString(R.string.localhost_url) + "challenge/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build();
        ChallengeApi challengeApi = retrofit.create(ChallengeApi.class);

        challengeApi.getChallenge(challengeId).enqueue(new Callback<Challenge>() {
            @Override
            public void onResponse(Call<Challenge> call, Response<Challenge> response) {
                challenge = response.body();
                initViews(view);

            }

            @Override
            public void onFailure(Call<Challenge> call, Throwable t) {

            }
        });
    }

    private class HeaderAdapter extends TableHeaderAdapter {

        HeaderAdapter(Context context) {
            super(context);
        }

        @Override
        public View getHeaderView(int columnIndex, ViewGroup parentView) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View convertView = null;
            switch (columnIndex) {
                case 0:
                    convertView = li.inflate(R.layout.table_row_tv, null);
                    TextView tv_sNo = (TextView) convertView;
                    tv_sNo.setText("Rank");
                    break;

                case 2:
                    convertView = li.inflate(R.layout.table_row_tv, null);

                    TextView tv_userName = (TextView) convertView;
                    tv_userName.setText("Opponents");
                    break;
                case 1:
                    convertView = li.inflate(R.layout.table_row_tv, null);
                    break;
                case 3:
                    convertView = li.inflate(R.layout.table_row_tv, null);
                    TextView tv_status = (TextView) convertView;
                    tv_status.setText("Status");
                    break;
                case 4:
                    convertView = li.inflate(R.layout.table_row_tv, null);
                    TextView tv_score = (TextView) convertView;
                    tv_score.setText("Score");
                    break;
            }

            return convertView;
        }
    }

    private class TableAdapter extends TableDataAdapter<User> {

        TableAdapter(Context context, List<User> data) {
            super(context, data);
        }

        @Override
        public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View convertView = null;
            switch (columnIndex) {
                case 0:
                    convertView = li.inflate(R.layout.table_row_tv, null);
                    TextView tv_sNo = (TextView) convertView;
                    tv_sNo.setText(String.valueOf(rowIndex + 1));
                    break;

                case 1:
                    convertView = li.inflate(R.layout.table_row_iv, null);
                    String iv_url = challenge.getUsersInGame().get(rowIndex).getImage_url();
                    //TODO: Set image
                    break;
                case 2:
                    convertView = li.inflate(R.layout.table_row_tv, null);

                    TextView tv_userName = (TextView) convertView;
                    tv_userName.setText(challenge.getUsersInGame().get(rowIndex).getName());
                    break;
                case 3:
                    convertView = li.inflate(R.layout.table_row_tv, null);
                    String text = "";
                    if (challenge.getStatuses().get(rowIndex)) {
                        text = "Complete";
                    } else {
                        text = "Incomplete";
                    }
                    TextView tv_status = (TextView) convertView;
                    tv_status.setText(text);
                    break;
                case 4:
                    convertView = li.inflate(R.layout.table_row_tv, null);

                    TextView tv_score = (TextView) convertView;
                    tv_score.setText(String.format(challenge.getScore().get(rowIndex).toString()));
                    break;
            }

            return convertView;
        }
    }
}
