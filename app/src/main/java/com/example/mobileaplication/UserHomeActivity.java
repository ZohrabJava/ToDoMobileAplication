package com.example.mobileaplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileaplication.model.User;
import com.example.mobileaplication.retrofit.HttpService;
import com.example.mobileaplication.retrofit.RetrofitService;
import com.example.mobileaplication.retrofit.UserApi;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeActivity extends AppCompatActivity {
    MaterialButton newTasks, inProgress, inReview, tasksDone, tasksRejected, logout;
    TextView userView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_activiy);

        initializeComponents();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomeActivity.this, LoginActivity.class));
            }
        });
        newTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomeActivity.this, NewTasksActivity.class));
            }
        });
        inProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomeActivity.this, InProgressTasks.class));
            }
        });
        inReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomeActivity.this, UserReviewTask.class));
            }
        });
        tasksDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomeActivity.this, UserDoneTasks.class));
            }
        });
        tasksRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHomeActivity.this, UserRejectedTasks.class));
            }
        });
    }

    private void initializeComponents() {
        newTasks = findViewById(R.id.buttonNew);
        inProgress = findViewById(R.id.buttonInProgres);
        inReview = findViewById(R.id.buttonInReview);
        tasksDone = findViewById(R.id.buttonDone);
        tasksRejected = findViewById(R.id.buttonRejected);
        logout = findViewById(R.id.buttonLogout);
        userView = findViewById(R.id.userText);
        User user = new User();

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        user.setUserName(HttpService.username);


        userApi.getUserByUsername(HttpService.token, user)
                .enqueue(new Callback<User>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse( Call<User> call,  Response<User> response) {
                        assert response.body() != null;
                        userView.clearComposingText();
                        userView.setText(response.body().getFirstName() + " " + response.body().getLastName());
                    }

                    @Override
                    public void onFailure( Call<User> call,  Throwable t) {
                        Toast.makeText(UserHomeActivity.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(UserHomeActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }
}