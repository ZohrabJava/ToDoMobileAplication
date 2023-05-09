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

import com.example.mobileaplication.model.User;
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

public class AllUsers extends AppCompatActivity {

    private List<User> userDtoList = new ArrayList<>();
    private List<String> userInfo = new ArrayList<>();
    public static User selectedDto = new User();
    MaterialButton goBack, logout;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        goBack = findViewById(R.id.userNewTaskGoBack);
        logout = findViewById(R.id.userInProgressTaskLogout);


        initializeComponents();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllUsers.this, LoginActivity.class));
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllUsers.this, AdminHomeActivity.class));
            }
        });
    }

    private void initializeComponents() {

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.getAllUsers(HttpService.token)
                .enqueue(new Callback<List<User>>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                        assert response.body() != null;
                        userDtoList.addAll(response.body());
                        for (User userDto : userDtoList) {
                            userInfo.add(userDto.getFirstName() + " " + userDto.getLastName() + "(" + userDto.getUserName() + ")");
                        }
                        LinearLayout container = findViewById(R.id.container);

                        for (User dto : userDtoList) {

                            View row = getLayoutInflater().inflate(R.layout.row_layout, container, false);
                            TextView textView = row.findViewById(R.id.text_view);
                            Button button = row.findViewById(R.id.button);
                            button.setText("Info");

                            textView.setText(dto.getFirstName() + " " + dto.getLastName() + "(" + dto.getUserName() + ")");
                            button.setId(dto.getUserId().intValue());

                            button.setOnClickListener(v -> {
                                selectedDto = Service.getUserDtoFromListById(userDtoList, (long) button.getId());
                                startActivity(new Intent(AllUsers.this, UserInfo.class));
                            });

                            container.addView(row);
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                        Toast.makeText(AllUsers.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(UserHomeActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);

                    }
                });
    }
}