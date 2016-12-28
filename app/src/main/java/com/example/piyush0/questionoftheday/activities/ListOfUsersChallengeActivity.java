package com.example.piyush0.questionoftheday.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.api.UserApi;
import com.example.piyush0.questionoftheday.dummy_utils.DummyUsers;
import com.example.piyush0.questionoftheday.models.User;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListOfUsersChallengeActivity extends AppCompatActivity {

    private RecyclerView usersRecyclerView;
    private Button btn_challenge;

    private ArrayList<User> users;
    private String selectedTopic;
    private Integer numOfQuestionsSelected;
    private ArrayList<String> usersChallenged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_users);

        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA,getWindow());

        getIntentExtras();
        fetchUsers();

    }

    private void setListenerToButtonChallenge() {
        btn_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent();
            }
        });
    }

    private void sendIntent() {

        Intent intent = new Intent(ListOfUsersChallengeActivity.this, WaitingForApprovalActivity.class);
        intent.putExtra("selectedTopic", selectedTopic);
        intent.putExtra("numOfQuestionsSelected", numOfQuestionsSelected);
        intent.putExtra("usersChallenged", usersChallenged);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        usersChallenged = new ArrayList<>();
    }

    private void fetchUsers() {
        String url = getResources().getString(R.string.localhost_url) + "users/";
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build() ;

        UserApi userApi = retrofit.create(UserApi.class);

        userApi.listUsers().enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                users = response.body();
                initViews();
                setListenerToButtonChallenge();
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });
    }

    private void initViews() {

        btn_challenge = (Button) findViewById(R.id.activity_list_of_users_btn_challenege);
        btn_challenge.setEnabled(false);
        usersRecyclerView = (RecyclerView) findViewById(R.id.activity_challenge_list_of_users);
        usersRecyclerView.setAdapter(new UserAdapter());
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        selectedTopic = intent.getStringExtra("selectedTopic");
        numOfQuestionsSelected = intent.getIntExtra("numOfQuestionsSelected", 0); // 0 is default value.
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {

        SmoothCheckBox checkBox;
        ImageView user_image;
        TextView tv_name, tv_score;

        UserViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = getLayoutInflater();

            View convertView = li.inflate(R.layout.list_item_user, null);

            UserViewHolder userViewHolder = new UserViewHolder(convertView);

            userViewHolder.tv_name = (TextView) convertView.findViewById(R.id.user_list_tv_name);
            userViewHolder.tv_score = (TextView) convertView.findViewById(R.id.user_list_tv_score);
            userViewHolder.user_image = (ImageView) convertView.findViewById(R.id.user_list_iv_userimage);
            userViewHolder.checkBox = (SmoothCheckBox) convertView.findViewById(R.id.list_item_user_challenge_checkbox);

            return userViewHolder;
        }

        @Override
        public void onBindViewHolder(final UserViewHolder holder, int position) {

            User user = users.get(position);

            holder.tv_name.setText(user.getName());
            holder.tv_score.setText(String.valueOf(user.getScore()));
            String image_url = user.getImage_url();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.checkBox.setChecked(!holder.checkBox.isChecked(), true);

                    if (holder.checkBox.isChecked()) {
                        usersChallenged.add(holder.tv_name.getText().toString());
                    } else {
                        usersChallenged.remove(holder.tv_name.getText().toString());
                    }

                    btn_challenge.setEnabled(usersChallenged.size() > 0);

                }
            });
            //TODO: Set image from url using picasso

        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }


}