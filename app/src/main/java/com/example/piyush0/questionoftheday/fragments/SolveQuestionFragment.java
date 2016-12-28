package com.example.piyush0.questionoftheday.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.CheckAnswer;
import com.example.piyush0.questionoftheday.utils.InitOptionsSelectedArray;
import com.example.piyush0.questionoftheday.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class SolveQuestionFragment extends Fragment {

    public static final String TAG = "Solve";

    private TextView tv_quesStatement;
    private RecyclerView optionsRecyclerView;
    private Button btn_sumbit;

    private Context context;

    private Question question;

    private Boolean isCorrectlySolved;


    private ArrayList<Boolean> optionsSelected;

    private Boolean isButtonActivated;
    private String source;

    public SolveQuestionFragment() {
        // Required empty public constructor
    }

    public static SolveQuestionFragment newInstance(Integer questionId, Boolean isButtonActivated, String source) {
        Bundle args = new Bundle();
        args.putInt("questionId", questionId);
        args.putBoolean("isButtonActivated", isButtonActivated);

        args.putString("source", source);
        SolveQuestionFragment solveQuestionFragment = new SolveQuestionFragment();
        solveQuestionFragment.setArguments(args);
        return solveQuestionFragment;
    }

    private void setClickListenerOnButton() {

        btn_sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isCorrectlySolved = CheckAnswer.isCorrect(optionsSelected, question);

                if (isCorrectlySolved) {
                    Toast.makeText(context, "Correct", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Incorrect", Toast.LENGTH_SHORT).show();
                }

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.
                        beginTransaction().
                        replace(R.id.content_main, ArchiveFragment.newInstance()).
                        commit();
            }

        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_solve_question, container, false);
        isButtonActivated = getArguments().getBoolean("isButtonActivated");

        this.source = getArguments().getString("source");
        if (source.equals("SolveTodayQuestionFragment")) {
            onBooleanArrayPasser = (OnBooleanArrayPass) getParentFragment();
        } else if (source.equals("GameActivity")) {
            onAttatchAct(getActivity());
        }

        initViews(view);
        getQuestion();
        initContext();
        setClickListenerOnButton();
        return view;
    }

    private void initContext() {
        context = getActivity().getBaseContext();
    }


    private void initViews(View view) {

        tv_quesStatement = (TextView) view.findViewById(R.id.fragment_question_tv_statement);
        btn_sumbit = (Button) view.findViewById(R.id.fragment_question_btn_submit);
        if (isButtonActivated) {
            btn_sumbit.setVisibility(View.VISIBLE);
        }
        optionsRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_question_options_list);
        optionsRecyclerView.setAdapter(new OptionAdapter());

        optionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        optionsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getContext()
        ));

        optionsSelected = InitOptionsSelectedArray.init(optionsSelected);

    }

    private void getQuestion() {
        if (source.equals("SolveTodayQuestionFragment")) {
            Realm realm = Realm.getDefaultInstance();
            question = realm.where(Question.class).equalTo("isToday", true).findFirst();

        } else {
            int questionId = getArguments().getInt("questionId");
            question = DummyQuestion.getDummyQuestions().get(questionId);
        }

        tv_quesStatement.setText(question.getQuestion());
    }

    public class OptionViewHolder extends RecyclerView.ViewHolder {

        SmoothCheckBox checkbox;
        TextView textView;

        OptionViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class OptionAdapter extends RecyclerView.Adapter<SolveQuestionFragment.OptionViewHolder> {

        @Override
        public SolveQuestionFragment.OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView = li.inflate(R.layout.list_item_options, parent, false);

            SolveQuestionFragment.OptionViewHolder optionViewHolder = new SolveQuestionFragment.OptionViewHolder(convertView);
            optionViewHolder.checkbox = (SmoothCheckBox) convertView.findViewById(R.id.list_item_option_checkbox);
            optionViewHolder.textView = (TextView) convertView.findViewById(R.id.list_item_option_textView);

            return optionViewHolder;
        }

        @Override
        public void onBindViewHolder(final SolveQuestionFragment.OptionViewHolder holder, final int position) {
            holder.checkbox.setChecked(false);
            holder.textView.setText(question.getOptions().get(position).getAnswer());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkbox.setChecked(!holder.checkbox.isChecked(), true);

                    if (holder.checkbox.isChecked()) {
                        optionsSelected.set(holder.getAdapterPosition(), true);
                    } else {
                        optionsSelected.set(holder.getAdapterPosition(), false);
                    }


                    pass();
                }
            });

        }

        @Override
        public int getItemCount() {
            return question.getOptions().size();
        }
    }

    public void pass() {
        if (!isButtonActivated) {
            onBooleanArrayPasser.onBooleanArrayPass(optionsSelected);
        }

        /*If button is not activated, checking must be taking in parent.*/
    }

    OnBooleanArrayPass onBooleanArrayPasser;


    public void onAttatchAct(Activity activity) {
        super.onAttach(activity);
        onBooleanArrayPasser = (OnBooleanArrayPass) activity;
    }


    public interface OnBooleanArrayPass {
        void onBooleanArrayPass(ArrayList<Boolean> optionsSelected);
    }


}
