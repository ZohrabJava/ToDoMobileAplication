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

public class UserRejectedTaskDetails extends AppCompatActivity {

    MaterialButton goBack, logout, viewComment, reviewerComment, reply;
    TextView title, description, startDate, endDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_refected_task_details);

        goBack = findViewById(R.id.userTaskDetailsGoBack);
        logout = findViewById(R.id.userNewTaskDetailsLogout);
        viewComment = findViewById(R.id.userTaskStart);
        reviewerComment = findViewById(R.id.reviewerComment);
        reply = findViewById(R.id.reply);
        title = findViewById(R.id.taskDetailsTitle);
        description = findViewById(R.id.taskDetailsDescription);
        startDate = findViewById(R.id.taskDetailsStartDate);
        endDate = findViewById(R.id.taskDetailsEndDate);

        title.clearComposingText();
        description.clearComposingText();
        startDate.clearComposingText();
        endDate.clearComposingText();

        title.setText(UserRejectedTasks.selectedDto.getTitle());
        description.setText(UserRejectedTasks.selectedDto.getDescription());
        startDate.setText(UserRejectedTasks.selectedDto.getStartDate());
        endDate.setText(UserRejectedTasks.selectedDto.getEndDate());

        viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(UserRejectedTasks.selectedDto.getComment());
            }
        });
        reviewerComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(UserRejectedTasks.selectedDto.getReviewerComment());
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserRejectedTaskDetails.this, LoginActivity.class));
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserRejectedTaskDetails.this, UserRejectedTasks.class));
            }
        });
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRejectedTasks.selectedDto.setStatusId(3L);

                showPopup2();

            }
        });

    }

    private void showPopup(String comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View popupView = getLayoutInflater().inflate(R.layout.popup_comment, null);

        builder.setView(popupView);

        TextView textView = popupView.findViewById(R.id.comment);

        AlertDialog dialog = builder.create();
        textView.setText(comment);

        dialog.show();
    }
    private void showPopup2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View popupView = getLayoutInflater().inflate(R.layout.popup_input, null);

        builder.setView(popupView);

        EditText inputText = popupView.findViewById(R.id.input_text);
        Button actionButton = popupView.findViewById(R.id.action_button);

        AlertDialog dialog = builder.create();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRejectedTasks.selectedDto.setComment(inputText.getText().toString());

                if (inputText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(UserRejectedTaskDetails.this, "Please fell answer comment", Toast.LENGTH_SHORT).show();
                } else {
                    accept();
                }
            }
        });

        dialog.show();
    }
    private void accept(){
        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.changeTaskStatus(HttpService.token, UserRejectedTasks.selectedDto)
                .enqueue(new Callback<MessageResponse>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        assert response.body() != null;
                        if (response.body().getMessage() == null) {
                            Toast.makeText(UserRejectedTaskDetails.this, "Task successful done", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserRejectedTaskDetails.this, UserRejectedTasks.class));
                        } else {
                            Toast.makeText(UserRejectedTaskDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Toast.makeText(UserRejectedTaskDetails.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(NewTaskDetails.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }
}