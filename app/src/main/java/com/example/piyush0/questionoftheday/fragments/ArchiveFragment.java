package com.example.piyush0.questionoftheday.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.api.QuestionApi;
import com.example.piyush0.questionoftheday.models.Question;
import com.mittsu.markedview.MarkedView;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArchiveFragment extends Fragment {

    public static final String TAG = "ArchiveFrag";

    private RecyclerView questionsRecyclerView;
    private ArchiveAdapter archiveAdapter;

    private ArrayList<Question> questions;

    private Context context;

    private View outerView;

    boolean nullCheck;

    public ArchiveFragment() {
        // Required empty public constructor
    }

    public static ArchiveFragment newInstance() {
        return new ArchiveFragment();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_menu_archive, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_filter) {
            showDialog();
            return true;
        } else if (id == R.id.action_refersh) {
            getDefaultQuestionsWithoutFilter();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        FilterDialogFragment filterDiagFrag = new FilterDialogFragment();
        filterDiagFrag.show(fragmentManager, "filter");
        filterDiagFrag.setOnSubmitListener(new FilterDialogFragment.OnSubmitListener() {
            @Override
            public void filtersSelected(ArrayList<String> filters, String selectedSort) {
                getQuestion(filters, selectedSort);
            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initContext();
        setHasOptionsMenu(true); /*This sets the menu.*/
        outerView = inflater.inflate(R.layout.fragment_archive, container, false);
        getDefaultQuestionsWithoutFilter();

        return outerView;
    }

    private void initContext() {
        context = getActivity().getBaseContext();
    }

    private void getDefaultQuestionsWithoutFilter() {
        String url = getResources().getString(R.string.localhost_url) + "questions/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build();
        QuestionApi questionApi = retrofit.create(QuestionApi.class);

        questionApi.listQuestions().enqueue(new Callback<ArrayList<Question>>() {
            @Override
            public void onResponse(Call<ArrayList<Question>> call, Response<ArrayList<Question>> response) {
                questions = response.body();
                if(questions!=null) {
                    initRecyclerView(outerView);
                } else {
                    Toast.makeText(getActivity(),"Error fetching the data from the server",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Question>> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                t.printStackTrace();
            }
        });

    }

    private void getQuestion(ArrayList<String> filter, String selectedSort) {
        String url = getResources().getString(R.string.localhost_url) + "questions/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build();
        QuestionApi questionApi = retrofit.create(QuestionApi.class);
        QuestionApi.Request request = new QuestionApi.Request(filter,selectedSort);

        questionApi.listQuestionWithFilter(request).enqueue(new Callback<ArrayList<Question>>() {
            @Override
            public void onResponse(Call<ArrayList<Question>> call, Response<ArrayList<Question>> response) {
                Log.d(TAG, "onResponse: " + response.body().get(0).getQuestion());

                if(response.body() != null) {
                    nullCheck = true;
                } else {
                    Toast.makeText(getActivity(),R.string.error_string,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Question>> call, Throwable t) {

            }
        });

        if(nullCheck == true) {
            archiveAdapter.notifyDataSetChanged();
        }
    }


    private void initRecyclerView(View view) {
        questionsRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_archive_list_questions);
        archiveAdapter = new ArchiveAdapter();
        questionsRecyclerView.setAdapter(archiveAdapter);
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private class ArchiveViewHolder extends RecyclerView.ViewHolder {

        MarkedView tv_question_statement;
        CardView cardView;
        Button btn_attempt;

        ArchiveViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ArchiveAdapter extends RecyclerView.Adapter<ArchiveViewHolder> {

        public ArchiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = li.inflate(R.layout.list_item_questions_archive, parent, false);

            ArchiveViewHolder archiveViewHolder = new ArchiveViewHolder(view);
            archiveViewHolder.tv_question_statement = (MarkedView) view.findViewById(R.id.item_list_archive_question_statement);
            archiveViewHolder.cardView = (CardView) view.findViewById(R.id.item_list_archive_card);
            archiveViewHolder.btn_attempt = (Button) view.findViewById(R.id.list_item_question_archive_btn_attempt);
            return archiveViewHolder;
        }

        public void onBindViewHolder(final ArchiveViewHolder holder, final int position) {

            Log.d(TAG, "onBindViewHolder: " + questions.get(position).getQuestion());
            holder.tv_question_statement.setMDText(questions.get(position).getQuestion());


            holder.btn_attempt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: item");
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.
                            beginTransaction().
                            replace(R.id.content_main,
                                    SolveQuestionFragment.newInstance(null,questions.get(holder.getAdapterPosition())
                                    , true,
                                    "ArchiveFragment")).
                            commit();

                }
            });
        }

        public int getItemCount() {
            return questions.size();
        }

    }
}
