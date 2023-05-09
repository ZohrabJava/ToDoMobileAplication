package com.example.mobileaplication;

import androidx.appcompat.app.AlertDialog;
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

public class AllTaskDetails extends AppCompatActivity {

    MaterialButton goBack, logout, viewComment, reviewerComment;
    TextView title, description, startDate, endDate, taskUser, taskStatus;

    private String comment;
    private String reviewComment;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_task_details);

        goBack = findViewById(R.id.userTaskDetailsGoBack);
        logout = findViewById(R.id.userNewTaskDetailsLogout);
        viewComment = findViewById(R.id.userTaskStart2);
        reviewerComment = findViewById(R.id.reviewComment);
        title = findViewById(R.id.taskDetailsTitle);
        description = findViewById(R.id.taskDetailsDescription);
        startDate = findViewById(R.id.taskDetailsStartDate);
        endDate = findViewById(R.id.taskDetailsEndDate);
        taskUser = findViewById(R.id.taskUser);
        taskStatus = findViewById(R.id.taskStatus);

        title.clearComposingText();
        description.clearComposingText();
        startDate.clearComposingText();
        endDate.clearComposingText();

        title.setText(AllTasks.selectedDto.getTitle());
        description.setText(AllTasks.selectedDto.getDescription());
        startDate.setText(AllTasks.selectedDto.getStartDate());
        endDate.setText(AllTasks.selectedDto.getEndDate());

        int status = AllTasks.selectedDto.getStatusId().intValue();
        String statusName = "";

        switch (status) {
            case 1:
                statusName = "New Task";
                viewComment.setVisibility(View.GONE);
                reviewerComment.setVisibility(View.GONE);
                break;
            case 2:
                statusName = "In Progress";
                viewComment.setVisibility(View.GONE);
                reviewerComment.setVisibility(View.GONE);
                break;
            case 3:
                statusName = "In Review";
                reviewerComment.setVisibility(View.GONE);
                break;
            case 4:
                statusName = "Rejected";
                break;
            case 5:
                statusName = "Done";
                break;
            default:
                statusName = "Status";
                break;
        }
        taskStatus.setText(statusName);

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);


        userApi.getUserByUsername2(HttpService.token, AllTasks.selectedDto.getUsername())
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
                        Toast.makeText(AllTaskDetails.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(NewTaskDetails.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });


        comment = AllTasks.selectedDto.getComment();
        reviewComment = AllTasks.selectedDto.getReviewerComment();

       if (status == 3 || status == 4 || status == 5 ){
           viewComment.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   showPopup(comment);
               }
           });
           if (status != 3){
               reviewerComment.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       showPopup(reviewComment);
                   }
               });
           }
       }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllTaskDetails.this, LoginActivity.class));
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllTaskDetails.this, AllTasks.class));
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

}