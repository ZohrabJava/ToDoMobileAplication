package com.example.mobileaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileaplication.model.User;
import com.example.mobileaplication.retrofit.HttpService;
import com.example.mobileaplication.retrofit.RetrofitService;
import com.example.mobileaplication.retrofit.UserApi;
import com.google.android.material.button.MaterialButton;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHomeActivity extends AppCompatActivity {
    MaterialButton createTask, allUsers, review, allTasks, logout;
    TextView userView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        initializeComponents();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
            }
        });
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, CreateNewTaskActivity.class));
            }
        });
        allUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, AllUsers.class));
            }
        });
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, AdminReviewTasks.class));
            }
        });
        allTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, AllTasks.class));
            }
        });
    }

    private void initializeComponents() {
        createTask = findViewById(R.id.createNewTaskButton);
        allUsers = findViewById(R.id.allUsersButton);
        review = findViewById(R.id.reviewButton);
        allTasks = findViewById(R.id.allTasksButton);
        logout = findViewById(R.id.buttonAdminLogout);
        userView = findViewById(R.id.adminName);
        User user = new User();

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        user.setUserName(HttpService.username);


        userApi.getUserByUsername(HttpService.token, user)
                .enqueue(new Callback<User>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        assert response.body() != null;
                        userView.clearComposingText();
                        userView.setText(response.body().getFirstName() + " " + response.body().getLastName());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(AdminHomeActivity.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(UserHomeActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }
}