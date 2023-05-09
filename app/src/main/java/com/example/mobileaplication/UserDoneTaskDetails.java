package com.example.mobileaplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class UserDoneTaskDetails extends AppCompatActivity {

    MaterialButton goBack, logout, viewComment, reviewerComment;
    TextView title, description, startDate, endDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_done_task_details);

        goBack = findViewById(R.id.userTaskDetailsGoBack);
        logout = findViewById(R.id.userNewTaskDetailsLogout);
        viewComment = findViewById(R.id.userTaskStart);
        reviewerComment = findViewById(R.id.reviewerComment);
        title = findViewById(R.id.taskDetailsTitle);
        description = findViewById(R.id.taskDetailsDescription);
        startDate = findViewById(R.id.taskDetailsStartDate);
        endDate = findViewById(R.id.taskDetailsEndDate);

        title.clearComposingText();
        description.clearComposingText();
        startDate.clearComposingText();
        endDate.clearComposingText();

        title.setText(UserDoneTasks.selectedDto.getTitle());
        description.setText(UserDoneTasks.selectedDto.getDescription());
        startDate.setText(UserDoneTasks.selectedDto.getStartDate());
        endDate.setText(UserDoneTasks.selectedDto.getEndDate());

        viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(UserDoneTasks.selectedDto.getComment());
            }
        });
        reviewerComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(UserDoneTasks.selectedDto.getReviewerComment());
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDoneTaskDetails.this, LoginActivity.class));
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserDoneTaskDetails.this, UserDoneTasks.class));
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