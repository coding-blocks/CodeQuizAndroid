package com.example.piyush0.questionoftheday.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.dummy_utils.DummyQuestion;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.FontsOverride;
import com.example.piyush0.questionoftheday.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;

public class GameResultsActivity extends AppCompatActivity {

    public static final String TAG = "GameResults";

    private TextView tv_time;
    private TableView tableView;

    private long timeTaken;
    private ArrayList<ArrayList<Integer>> optionsSelected;
    private ArrayList<Question> questions;
    private ArrayList<Boolean> correctsAndIncorrects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_results);

        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA,getWindow());
        getQuestions();
        getIntentExtras();
        initViews();
    }

    private void getQuestions() {
        questions = DummyQuestion.getDummyQuestions();
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        Long zero = 0L;
        timeTaken = intent.getLongExtra("timeForGame", zero);
        optionsSelected = (ArrayList<ArrayList<Integer>>) intent.getSerializableExtra("optionsYouSelected");
        correctsAndIncorrects = (ArrayList<Boolean>) intent.getSerializableExtra("correctsAndIncorrects");
    }

    private void initViews() {
        tv_time = (TextView) findViewById(R.id.activity_game_results_tv_time);
        String text = TimeUtil.getMinutesAndSecond(timeTaken).get(0) + TimeUtil.getMinutesAndSecond(timeTaken).get(1);
        tv_time.setText(text);
        initTableView();
    }

    private void initTableView() {
        tableView = (TableView) findViewById(R.id.activity_game_results_tableView);
        TableColumnWeightModel columnModel = new TableColumnWeightModel(5);
        columnModel.setColumnWeight(0, 1);
        columnModel.setColumnWeight(1, 8);
        columnModel.setColumnWeight(2, 5);
        columnModel.setColumnWeight(3, 5);
        columnModel.setColumnWeight(4, 4);
        tableView.setColumnModel(columnModel);
        tableView.setDataAdapter(new TableAdapter(this, questions));

         /*I dont understand the code written below. Its was suggested as a hack on github as Header Adapter was not performing well*/

        ViewTreeObserver vto = tableView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tableView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                tableView.setHeaderAdapter(new HeaderAdapter(GameResultsActivity.this));
            }
        });
    }

    private class TableAdapter extends TableDataAdapter<Question> {


        TableAdapter(Context context, List<Question> data) {
            super(context, data);
        }

        @Override
        public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
            LayoutInflater li = getLayoutInflater();

            View convertView = null;
            Question question = questions.get(rowIndex);
            ArrayList<Integer> options = optionsSelected.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    convertView = li.inflate(R.layout.table_row_tv, null);
                    TextView tv_sNo = (TextView) convertView;
                    tv_sNo.setText(String.valueOf(rowIndex + 1));
                    break;

                case 1:
                    convertView = li.inflate(R.layout.table_row_tv, null);
                    TextView tv_question = (TextView) convertView;
                    tv_question.setText(question.getQuestion());
                    break;

                case 2:

                    String text = "";

                    for (int i = 0; i < options.size(); i++) {
                        text += question.getOptions().get(options.get(i)).getAnswer() + "\n";
                    }

                    convertView = li.inflate(R.layout.table_row_tv, null);
                    TextView tv_youChose = (TextView) convertView;
                    tv_youChose.setText(text);

                    break;
                case 3:
                    String correctOptions = "";

                    for (int i = 0; i < question.getOptions().size(); i++) {
                        if (question.getOptions().get(i).isCorrect()) {
                            correctOptions += question.getOptions().get(i).getAnswer() + "\n";
                        }
                    }
                    convertView = li.inflate(R.layout.table_row_tv, null);
                    TextView tv_correct = (TextView) convertView;
                    tv_correct.setText(correctOptions);

                    break;

                case 4:
                    convertView = li.inflate(R.layout.table_row_tv, null);
                    TextView tv_isCorrect = (TextView) convertView;

                    String isCorrect = "";

                    if(correctsAndIncorrects.get(rowIndex)){
                        isCorrect = "Correct";
                    }
                    else{
                        isCorrect = "Incorrect";
                    }

                    tv_isCorrect.setText(isCorrect);

                    break;
            }


            return convertView;
        }
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

                    break;

                case 1:
                    convertView = li.inflate(R.layout.table_row_tv, null);

                    TextView tv_question = (TextView) convertView;
                    tv_question.setText("Question");
                    break;

                case 2:
                    convertView = li.inflate(R.layout.table_row_tv, null);

                    TextView tv_youChose = (TextView) convertView;
                    tv_youChose.setText("Your Answer");
                    break;

                case 3:
                    convertView = li.inflate(R.layout.table_row_tv, null);
                    TextView tv_correctAnswer = (TextView) convertView;
                    tv_correctAnswer.setText("Correct Answer");
                    break;
                case 4:
                    convertView = li.inflate(R.layout.table_row_tv, null);
                    break;
            }

            return convertView;
        }
    }

}
