package com.example.piyush0.questionoftheday.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.activities.ListOfUsersChallengeActivity;
import com.example.piyush0.questionoftheday.dummy_utils.NumberOfOptions;
import com.example.piyush0.questionoftheday.dummy_utils.DummyTopics;
import com.piotrek.customspinner.CustomSpinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeFragment extends Fragment {

    private Context context;

    private Button chooseOpponentButton;
    private CustomSpinner topicsSpinner;
    private CustomSpinner numOfQuestionSpinner;

    private String selectedTopic;
    private Integer numOfQuestionsSelected;

    public ChallengeFragment() {
        // Required empty public constructor
    }

    public static ChallengeFragment newInstance() {
        return new ChallengeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initContext();

        View view = inflater.inflate(R.layout.fragment_challenge, container, false);
        numOfQuestionsSelected = new Integer(0);
        selectedTopic = new String("");
        initViews(view);
        initButtonListener();
        initTopicAdapter();
        initNumOfQuesAdapter();

        return view;
    }

    private void initContext() {
        context = getActivity().getBaseContext();
    }

    private void initViews(View view) {

        chooseOpponentButton = (Button) view.findViewById(R.id.challenge_fragment_opponent_button);

        /*Since no topic or numQues is selected, the button is disabled.*/
        chooseOpponentButton.setEnabled(false);

        numOfQuestionSpinner = (CustomSpinner) view.findViewById(R.id.challenge_fragment_noOfQues_spinner);
        topicsSpinner = (CustomSpinner) view.findViewById(R.id.challenge_fragment_topic_spinner);

    }

    private void initTopicAdapter() {

        final String hintText = "Select a topic...";
        topicsSpinner.initializeStringValues((DummyTopics.getTopics().
                        toArray(new String[DummyTopics.getTopics().size()])),
                hintText);

        topicsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getSelectedItem().toString().equals(hintText)) {
                    selectedTopic = adapterView.getSelectedItem().toString();
                    if (numOfQuestionsSelected != 0) {
                        chooseOpponentButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initNumOfQuesAdapter() {

        final String hintText = "Select Number of Questions...";
        numOfQuestionSpinner.initializeStringValues(NumberOfOptions.
                        getNumberOfOptions().
                        toArray(new String[NumberOfOptions.getNumberOfOptions().size()]),
                hintText);

        numOfQuestionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getSelectedItem().toString().equals(hintText)) {
                    numOfQuestionsSelected = Integer.valueOf(adapterView.getSelectedItem().toString());
                    if (selectedTopic != null) {
                        chooseOpponentButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initButtonListener() {
        chooseOpponentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListOfUsersChallengeActivity.class);
                intent.putExtra("selectedTopic", selectedTopic);
                intent.putExtra("numOfQuestionsSelected", numOfQuestionsSelected);
                startActivity(intent);
            }
        });

    }

}
