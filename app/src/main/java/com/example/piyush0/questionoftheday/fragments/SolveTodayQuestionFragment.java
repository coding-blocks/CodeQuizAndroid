package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.models.Option;
import com.example.piyush0.questionoftheday.services.TimeCountingService;
import com.example.piyush0.questionoftheday.activities.MainActivity;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.CheckAnswer;
import com.example.piyush0.questionoftheday.utils.InitOptionsSelectedArray;
import com.example.piyush0.questionoftheday.utils.Refresh;
import com.example.piyush0.questionoftheday.utils.SimpleDividerItemDecoration;
import com.example.piyush0.questionoftheday.utils.TimeUtil;
import com.mittsu.markedview.MarkedView;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SolveTodayQuestionFragment extends Fragment {

    public static final String SHARED_PREF_NAME = "TodaySolved";
    public static final String TAG = "SolveToday";

    private Context context;

    private TextView tv_attemptsRemaining, tv_clock_seconds, tv_clock_minutes;
    private MarkedView tv_quesStatement;
    private RecyclerView optionsRecyclerView;
    private Button btn_sumbit;

    private Handler handler; /*This is being used to calculate the time*/

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FragmentManager fragmentManager;

    private Question todaysQuestion;

    private boolean isCorrectlySolved;
    private Long timeTaken;
    private int attempts;

    private RealmList<Option> options;


    public SolveTodayQuestionFragment() {

    }

    public static SolveTodayQuestionFragment newInstance() {

        return new SolveTodayQuestionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getTodaysQuestion();
        initContext();
        View view = inflater.inflate(R.layout.fragment_solve_today_question, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initViews(view);
        setClickListenerOnBtn();
        return view;
    }

    private void setClickListenerOnBtn() {

        btn_sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attempts = sharedPreferences.getInt("attempts", 0);
                attempts++;
                editor.putInt("attempts", attempts);

                tv_attemptsRemaining.setText(String.valueOf(3 - attempts));
                Log.d(TAG, "onClick: " + getOptionsSelected());
                isCorrectlySolved = CheckAnswer.isCorrect(getOptionsSelected(), todaysQuestion);
                editor.putBoolean("isCorrect", isCorrectlySolved);
                editor.commit();

                Refresh.refresh(sharedPreferences, fragmentManager, getContext());
            }
        });
    }

    private ArrayList<Boolean> getOptionsSelected() {
        ArrayList<Boolean> retVal = new ArrayList<>();
        for (Option o : options) {
            retVal.add(o.isSelected());
        }
        return retVal;
    }

    private void initContext() {

        context = getActivity().getBaseContext();
    }

    @Override
    public void onResume() {

        super.onResume();
        initSharedPrefs();
        stopTimeCountingService();
        initClock();
    }

    private void initClock() {

        handler = new Handler();
        handler.post(runnable);
    }

    private void initSharedPrefs() {

        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Long zero = 0L;
        timeTaken = sharedPreferences.getLong("timeForTodayQues", zero);
    }

    private void stopTimeCountingService() {

        Intent intent = new Intent(getContext(), TimeCountingService.class);
        context.stopService(intent);
    }

    private void initViews(View view) {

        tv_clock_minutes = (TextView) view.findViewById(R.id.fragment_solve_today_question_minute);
        tv_clock_seconds = (TextView) view.findViewById(R.id.fragment_solve_today_question_second);
        tv_attemptsRemaining = (TextView) view.findViewById(R.id.fragment_solve_today_question_attempts);
        tv_quesStatement = (MarkedView) view.findViewById(R.id.fragment_question_tv_statement);
        tv_quesStatement.setMDText(todaysQuestion.getQuestion());
        optionsRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_question_options_list);
        optionsRecyclerView.setAdapter(new OptionAdapter());
        optionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        optionsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        btn_sumbit = (Button) view.findViewById(R.id.fragment_question_btn_submit);

        initSharedPrefsOnCreate();
    }

    public class OptionViewHolder extends RecyclerView.ViewHolder {

        SmoothCheckBox checkbox;
        TextView textView;

        OptionViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class OptionAdapter extends RecyclerView.Adapter<OptionViewHolder> {

        @Override
        public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView = li.inflate(R.layout.list_item_options, parent, false);

            OptionViewHolder optionViewHolder = new OptionViewHolder(convertView);
            optionViewHolder.checkbox = (SmoothCheckBox) convertView.findViewById(R.id.list_item_option_checkbox);
            optionViewHolder.textView = (TextView) convertView.findViewById(R.id.list_item_option_textView);

            return optionViewHolder;
        }


        @Override
        public void onBindViewHolder(final OptionViewHolder holder, final int position) {
            holder.checkbox.setChecked(false);
            holder.textView.setText(todaysQuestion.getOptions().get(position).getAnswer());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkbox.setChecked(!holder.checkbox.isChecked(), true);

                    if (holder.checkbox.isChecked()) {
//                        todaysQuestion.getOptions().get(holder.getAdapterPosition()).setSelected(true);
                        options.get(holder.getAdapterPosition()).setSelected(true);
                        Log.d(TAG, "onClick: " + todaysQuestion.getOptions().get(holder.getAdapterPosition()).isSelected());
                    } else {
//                        todaysQuestion.getOptions().get(holder.getAdapterPosition()).setSelected(false);
                        options.get(holder.getAdapterPosition()).setSelected(false);
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return todaysQuestion.getOptions().size();
        }
    }

    private void getTodaysQuestion() {
        Realm realm = Realm.getDefaultInstance();
        Log.d(TAG, "getTodaysQuestion: " + realm.where(Question.class).findAll());
        todaysQuestion = realm.where(Question.class).equalTo("isToday", true).findFirst();
        Log.d(TAG, "getTodaysQuestion: " + todaysQuestion);
        options = realm.copyFromRealm(todaysQuestion).getOptions();
    }

    private void initSharedPrefsOnCreate() {

        sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        attempts = sharedPreferences.getInt("attempts", 0);
        tv_attemptsRemaining.setText(String.valueOf(3 - attempts));
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            tv_clock_minutes.setText(TimeUtil.getMinutesAndSecond(timeTaken).get(0));
            tv_clock_seconds.setText(TimeUtil.getMinutesAndSecond(timeTaken).get(1));
            timeTaken = timeTaken + 1000;
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onPause() {

        saveTimeTillNow();
        stopClock();
        startTimeCountingService();
        super.onPause();
    }

    private void saveTimeTillNow() {

        editor.putLong("timeForTodayQues", timeTaken);
        editor.commit();
    }

    private void stopClock() {

        handler.removeCallbacks(runnable);
    }

    private void startTimeCountingService() {

        Intent intent = new Intent(getContext(), TimeCountingService.class);
        intent.putExtra("timeForTodayQues", timeTaken);
        context.startService(intent);
    }


}
