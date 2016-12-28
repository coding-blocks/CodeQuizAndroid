package com.example.piyush0.questionoftheday.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.dummy_utils.DummyTopics;
import com.piotrek.customspinner.CustomSpinner;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by piyush0 on 09/12/16.
 */

public class FilterDialogFragment extends DialogFragment {

    public static final String DATE_SORT = "Date Added";
    public static final String DIFFICULTY_SORT = "Difficulty";

    private OnSubmitListener onSubmitListener;

    private RecyclerView topicsRecyclerView;
    private Button btn_submit;
    private CustomSpinner sortBySpinner;

    private ArrayList<String> topics;

    private ArrayList<Boolean> filtersSelectedBoolean; /*This is a helper array which is later used to create filterSelected Array*/

    private ArrayList<String> filtersSelected;
    private String selectedSort;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_dialog, null);
        getTopics();
        initFilterSelectedArrays();
        initViews(view);
        setClickListenerOnButton();
        return view;
    }

    private void initFilterSelectedArrays() {
        filtersSelectedBoolean = new ArrayList<>();
        filtersSelected = new ArrayList<>();

                /*Initialise the boolean array with all false.*/
        for (int i = 0; i < topics.size(); i++) {
            filtersSelectedBoolean.add(false);
        }
    }

    private void getTopics() {
        topics = DummyTopics.getTopics();
    }

    protected void setOnSubmitListener(OnSubmitListener var) {
        this.onSubmitListener = var;
    }

    private void initViews(View view) {

        btn_submit = (Button) view.findViewById(R.id.filter_dialog_frag_submit);

        /*Button is disabled because no sort is added yet.*/
        btn_submit.setEnabled(false);

        topicsRecyclerView = (RecyclerView) view.findViewById(R.id.filter_dialog_recycler_view);
        topicsRecyclerView.setAdapter(new FilterAdapter());
        topicsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sortBySpinner = (CustomSpinner) view.findViewById(R.id.filter_dialog_frag_sortBy_spinner);
        initSortAdapter();

    }

    private void setClickListenerOnButton() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*If booleanPosition is true, we add that topic to the resultant array*/

                for (int i = 0; i < topics.size(); i++) {
                    if (filtersSelectedBoolean.get(i)) {
                        filtersSelected.add(topics.get(i));
                    }
                }

                onSubmitListener.filtersSelected(filtersSelected, selectedSort);
                dismiss();
            }
        });
    }

    private void initSortAdapter() {

        ArrayList<String> sorts = new ArrayList<>();
        sorts.add(DATE_SORT);
        sorts.add(DIFFICULTY_SORT);

        final String hintText = "Sort By...";
        sortBySpinner.initializeStringValues(sorts.toArray(new String[sorts.size()]), hintText);
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getSelectedItem().toString().equals(hintText)) {
                    selectedSort = adapterView.getSelectedItem().toString();
                    btn_submit.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    private class FilterViewHolder extends RecyclerView.ViewHolder {
        SmoothCheckBox checkBox;
        TextView textView;

        FilterViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class FilterAdapter extends RecyclerView.Adapter<FilterViewHolder> {

        @Override
        public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView = li.inflate(R.layout.list_item_filters, parent, false);

            FilterViewHolder filterViewHolder = new FilterViewHolder(convertView);
            filterViewHolder.checkBox = (SmoothCheckBox) convertView.findViewById(R.id.list_item_filter_checkBox);
            filterViewHolder.textView = (TextView) convertView.findViewById(R.id.list_item_filter_textView);

            return filterViewHolder;
        }

        @Override
        public void onBindViewHolder(final FilterViewHolder holder, int position) {
            holder.checkBox.setChecked(false);
            holder.textView.setText(topics.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);
                    if (holder.checkBox.isChecked()) {
                        filtersSelectedBoolean.set(holder.getAdapterPosition(), true);
                    } else {
                        filtersSelectedBoolean.set(holder.getAdapterPosition(), false);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return topics.size();
        }
    }

    protected interface OnSubmitListener {
        void filtersSelected(ArrayList<String> filters, String selectedSort);
    }

}
