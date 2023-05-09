package com.example.mobileaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileaplication.model.MessageResponse;
import com.example.mobileaplication.retrofit.HttpService;
import com.example.mobileaplication.retrofit.RetrofitService;
import com.example.mobileaplication.retrofit.UserApi;
import com.google.android.material.button.MaterialButton;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewTaskDetails extends AppCompatActivity {

    MaterialButton goBack, logout, start;
    TextView title, description, startDate, endDate;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_details);

        goBack = findViewById(R.id.userTaskDetailsGoBack);
        logout = findViewById(R.id.userNewTaskDetailsLogout);
        start = findViewById(R.id.userTaskStart);
        title = findViewById(R.id.taskDetailsTitle);
        description = findViewById(R.id.taskDetailsDescription);
        startDate = findViewById(R.id.taskDetailsStartDate);
        endDate = findViewById(R.id.taskDetailsEndDate);


        title.clearComposingText();
        description.clearComposingText();
        startDate.clearComposingText();
        endDate.clearComposingText();

        title.setText(NewTasksActivity.selectedDto.getTitle());
        description.setText(NewTasksActivity.selectedDto.getDescription());
        startDate.setText(NewTasksActivity.selectedDto.getStartDate());
        endDate.setText(NewTasksActivity.selectedDto.getEndDate());

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTasksActivity.selectedDto.setStatusId(2L);
                userApi.changeTaskStatus(HttpService.token,  NewTasksActivity.selectedDto)
                        .enqueue(new Callback<MessageResponse>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                                assert response.body() != null;
                                if (response.body().getMessage() == null){
                                    Toast.makeText(NewTaskDetails.this, "Task in progress", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(NewTaskDetails.this, NewTasksActivity.class));
                                }else {
                                    Toast.makeText(NewTaskDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<MessageResponse> call, Throwable t) {
                                Toast.makeText(NewTaskDetails.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                                Logger.getLogger(NewTaskDetails.class.getName()).log(Level.SEVERE, "Error occurred", t);
                            }
                        });
                startActivity(new Intent(NewTaskDetails.this, NewTasksActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewTaskDetails.this, LoginActivity.class));
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewTaskDetails.this, NewTasksActivity.class));
            }
        });
    }
}