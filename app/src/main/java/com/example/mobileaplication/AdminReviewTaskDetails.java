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

public class AdminReviewTaskDetails extends AppCompatActivity {

    MaterialButton goBack, logout, viewComment;
    TextView title, description, startDate, endDate, taskUser;

    private String comment;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_review_task_details);

        goBack = findViewById(R.id.userTaskDetailsGoBack);
        logout = findViewById(R.id.userNewTaskDetailsLogout);
        viewComment = findViewById(R.id.userTaskStart);
        title = findViewById(R.id.taskDetailsTitle);
        description = findViewById(R.id.taskDetailsDescription);
        startDate = findViewById(R.id.taskDetailsStartDate);
        endDate = findViewById(R.id.taskDetailsEndDate);
        taskUser = findViewById(R.id.taskUser);

        title.clearComposingText();
        description.clearComposingText();
        startDate.clearComposingText();
        endDate.clearComposingText();

        title.setText(AdminReviewTasks.selectedDto.getTitle());
        description.setText(AdminReviewTasks.selectedDto.getDescription());
        startDate.setText(AdminReviewTasks.selectedDto.getStartDate());
        endDate.setText(AdminReviewTasks.selectedDto.getEndDate());


        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);


        userApi.getUserByUsername2(HttpService.token, AdminReviewTasks.selectedDto.getUsername())
                .enqueue(new Callback<User>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        assert response.body() != null;
                        User user = response.body();
                        taskUser.setText(user.getFirstName() + " " + user.getLastName() + " (" + user.getUserName() + ")");
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(AdminReviewTaskDetails.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(NewTaskDetails.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });


        comment = AdminReviewTasks.selectedDto.getComment();


        viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminReviewTaskDetails.this, LoginActivity.class));
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminReviewTaskDetails.this, AdminReviewTasks.class));
            }
        });
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View popupView = getLayoutInflater().inflate(R.layout.popup_admin_review, null);

        builder.setView(popupView);

        TextView textView = popupView.findViewById(R.id.comment);
        Button reject = popupView.findViewById(R.id.reject);
        Button done = popupView.findViewById(R.id.done);

        AlertDialog dialog = builder.create();
        textView.setText(comment);

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminReviewTasks.selectedDto.setStatusId(4L);

                showPopup2();

                dialog.dismiss();

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminReviewTasks.selectedDto.setStatusId(5L);

                showPopup2();

                dialog.dismiss();

            }
        });
        dialog.show();
    }
    private void showPopup2() {
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
                AdminReviewTasks.selectedDto.setReviewerComment(inputText.getText().toString());

                if (inputText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AdminReviewTaskDetails.this, "Please fell reviewer comment", Toast.LENGTH_SHORT).show();
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

        userApi.changeTaskStatus(HttpService.token, AdminReviewTasks.selectedDto)
                .enqueue(new Callback<MessageResponse>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        assert response.body() != null;
                        if (response.body().getMessage() == null) {
                            Toast.makeText(AdminReviewTaskDetails.this, "Task successful done", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminReviewTaskDetails.this, AdminReviewTasks.class));
                        } else {
                            Toast.makeText(AdminReviewTaskDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Toast.makeText(AdminReviewTaskDetails.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(NewTaskDetails.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }
}