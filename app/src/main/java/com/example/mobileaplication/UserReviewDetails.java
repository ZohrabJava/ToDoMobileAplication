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

public class UserReviewDetails extends AppCompatActivity {

    MaterialButton goBack, logout, viewComment;
    TextView title, description, startDate, endDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review_details);

        goBack = findViewById(R.id.userTaskDetailsGoBack);
        logout = findViewById(R.id.userNewTaskDetailsLogout);
        viewComment = findViewById(R.id.userTaskStart);
        title = findViewById(R.id.taskDetailsTitle);
        description = findViewById(R.id.taskDetailsDescription);
        startDate = findViewById(R.id.taskDetailsStartDate);
        endDate = findViewById(R.id.taskDetailsEndDate);

        title.clearComposingText();
        description.clearComposingText();
        startDate.clearComposingText();
        endDate.clearComposingText();

        title.setText(UserReviewTask.selectedDto.getTitle());
        description.setText(UserReviewTask.selectedDto.getDescription());
        startDate.setText(UserReviewTask.selectedDto.getStartDate());
        endDate.setText(UserReviewTask.selectedDto.getEndDate());

        viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserReviewDetails.this, LoginActivity.class));
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserReviewDetails.this, UserReviewTask.class));
            }
        });
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View popupView = getLayoutInflater().inflate(R.layout.popup_comment, null);

        builder.setView(popupView);

        TextView textView = popupView.findViewById(R.id.comment);

        AlertDialog dialog = builder.create();
        textView.setText(UserReviewTask.selectedDto.getComment());

        dialog.show();
    }
}