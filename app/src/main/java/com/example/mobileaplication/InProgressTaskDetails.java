package com.example.mobileaplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class InProgressTaskDetails extends AppCompatActivity {
    MaterialButton goBack, logout, finish;
    TextView title, description, startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_progress_task_details);

        goBack = findViewById(R.id.userTaskDetailsGoBack);
        logout = findViewById(R.id.userInProgressTaskLogout);
        finish = findViewById(R.id.userTaskStart);
        title = findViewById(R.id.taskDetailsTitle);
        description = findViewById(R.id.taskDetailsDescription);
        startDate = findViewById(R.id.taskDetailsStartDate);
        endDate = findViewById(R.id.taskDetailsEndDate);


        title.clearComposingText();
        description.clearComposingText();
        startDate.clearComposingText();
        endDate.clearComposingText();

        title.setText(InProgressTasks.selectedDto.getTitle());
        description.setText(InProgressTasks.selectedDto.getDescription());
        startDate.setText(InProgressTasks.selectedDto.getStartDate());
        endDate.setText(InProgressTasks.selectedDto.getEndDate());

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InProgressTaskDetails.this, LoginActivity.class));
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InProgressTaskDetails.this, InProgressTasks.class));
            }
        });
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View popupView = getLayoutInflater().inflate(R.layout.popup_input, null);

        builder.setView(popupView);

        EditText inputText = popupView.findViewById(R.id.input_text);
        Button actionButton = popupView.findViewById(R.id.action_button);

        AlertDialog dialog = builder.create();

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InProgressTasks.selectedDto.setStatusId(3L);
                InProgressTasks.selectedDto.setComment(inputText.getText().toString());

                if (inputText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(InProgressTaskDetails.this, "Please fell comment", Toast.LENGTH_SHORT).show();
                } else {
                    userApi.changeTaskStatus(HttpService.token, InProgressTasks.selectedDto)
                            .enqueue(new Callback<MessageResponse>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                                    assert response.body() != null;
                                    if (response.body().getMessage() == null) {
                                        Toast.makeText(InProgressTaskDetails.this, "Task send to review", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(InProgressTaskDetails.this, InProgressTasks.class));
                                    } else {
                                        Toast.makeText(InProgressTaskDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<MessageResponse> call, Throwable t) {
                                    Toast.makeText(InProgressTaskDetails.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                                    Logger.getLogger(NewTaskDetails.class.getName()).log(Level.SEVERE, "Error occurred", t);
                                }
                            });

                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }
}