package com.example.mobileaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileaplication.model.User;
import com.example.mobileaplication.model.UserInfoDto;
import com.example.mobileaplication.retrofit.HttpService;
import com.example.mobileaplication.retrofit.RetrofitService;
import com.example.mobileaplication.retrofit.UserApi;
import com.google.android.material.button.MaterialButton;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfo extends AppCompatActivity {
    MaterialButton goBack, logout;
    TextView name, notStarted, inProgress, inReview, rejected, done;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        goBack = findViewById(R.id.userNewTaskGoBack);
        logout = findViewById(R.id.userInProgressTaskLogout);

        name = findViewById(R.id.firstNameLastName);
        notStarted = findViewById(R.id.notStarted);
        inProgress = findViewById(R.id.inProgress);
        inReview = findViewById(R.id.inReview);
        rejected = findViewById(R.id.rejected);
        done = findViewById(R.id.done);

        name.setText(AllUsers.selectedDto.getFirstName() + " " + AllUsers.selectedDto.getLastName());

        initializeComponents();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserInfo.this, LoginActivity.class));
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserInfo.this, AllUsers.class));
            }
        });
    }

    private void initializeComponents() {

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);


        userApi.getUserInfoByUsername(HttpService.token, AllUsers.selectedDto.getUserName())
                .enqueue(new Callback<UserInfoDto>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<UserInfoDto> call, Response<UserInfoDto> response) {
                        assert response.body() != null;
                        notStarted.setText(response.body().getNewTaskCount() + " task not started");
                        inProgress.setText(response.body().getInProgressTaskCount() + " task in progress");
                        inReview.setText(response.body().getInReviewTaskCount() + " task in review");
                        rejected.setText(response.body().getRejectedTaskCount() + " task rejected");
                        done.setText(response.body().getDoneTaskCount() + " task done");
                    }

                    @Override
                    public void onFailure(Call<UserInfoDto> call, Throwable t) {
                        Toast.makeText(UserInfo.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(UserInfo.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }
}