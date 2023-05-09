package com.example.mobileaplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileaplication.model.TaskDto;
import com.example.mobileaplication.retrofit.HttpService;
import com.example.mobileaplication.retrofit.RetrofitService;
import com.example.mobileaplication.retrofit.UserApi;
import com.example.mobileaplication.service.Service;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InProgressTasks extends AppCompatActivity {
    private List<TaskDto> taskDtoList = new ArrayList<>();
    private List<String> taskTitle = new ArrayList<>();
    public static TaskDto selectedDto = new TaskDto();
    MaterialButton goBack, logout;
    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_progress_tasks);

        goBack = findViewById(R.id.userInProgressTaskGoBack);
        logout = findViewById(R.id.userInProgressTaskLogout);


        initializeComponents();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InProgressTasks.this, LoginActivity.class));
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InProgressTasks.this, UserHomeActivity.class));
            }
        });
    }

    private void initializeComponents() {

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.getTasksByUsernameAndStatus(HttpService.token, HttpService.username, 2L)
                .enqueue(new Callback<List<TaskDto>>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<List<TaskDto>> call, @NonNull Response<List<TaskDto>> response) {
                        assert response.body() != null;
                        taskDtoList.addAll(response.body());
                        for (TaskDto taskDto : taskDtoList) {
                            taskTitle.add(taskDto.getTitle());
                        }
                        LinearLayout container = findViewById(R.id.container);

                        for (TaskDto dto : taskDtoList) {

                            View row = getLayoutInflater().inflate(R.layout.row_layout, container, false);
                            TextView textView = row.findViewById(R.id.text_view);
                            Button button = row.findViewById(R.id.button);

                            textView.setText(dto.getTitle());
                            button.setId(dto.getTaskId().intValue());

                            button.setOnClickListener(v -> {
                                selectedDto = Service.getTaskDtoFromListById(taskDtoList, (long) button.getId());
                                startActivity(new Intent(InProgressTasks.this, InProgressTaskDetails.class));
                            });

                            container.addView(row);
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<TaskDto>> call, @NonNull Throwable t) {
                        Toast.makeText(InProgressTasks.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(UserHomeActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);

                    }
                });
    }
}