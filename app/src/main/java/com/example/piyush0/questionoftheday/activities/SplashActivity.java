package com.example.piyush0.questionoftheday.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.models.Question;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {

    public static final String TAG = "Splash";
    EditText et_email, et_password;
    Button btn_submit, btn_forgotPass, btn_signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Realm realm = Realm.getDefaultInstance();
        Log.d(TAG, "onCreate: " + realm.where(Question.class).findAll());
        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA,getWindow());

        init();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void init(){

        btn_submit = (Button) findViewById(R.id.splash_activity_btn_submit);


    }
}
