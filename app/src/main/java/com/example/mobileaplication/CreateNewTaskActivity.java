package com.example.mobileaplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mobileaplication.model.MessageResponse;
import com.example.mobileaplication.model.TaskDto;
import com.example.mobileaplication.model.User;
import com.example.mobileaplication.retrofit.HttpService;
import com.example.mobileaplication.retrofit.RetrofitService;
import com.example.mobileaplication.retrofit.UserApi;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CreateNewTaskActivity extends AppCompatActivity {

    MaterialButton dateButton, goBackButton, logout, register;
    TextInputEditText title, description;
    LocalDate endDate;

    LocalDate startDate = LocalDate.now();
    Spinner userListDropdown;
    List<String> userList = new ArrayList<>();
    String user;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_task);

        userList.add("empty");
        initializeComponents();

        dateButton = findViewById(R.id.dateButton);
        register = findViewById(R.id.registerTask);
        goBackButton = findViewById(R.id.createTaskGoBack);
        logout = findViewById(R.id.createTaskLogout);
        userListDropdown = findViewById(R.id.userList);
        title = findViewById(R.id.createTaskTitle);
        description = findViewById(R.id.editTextTextMultiLine);

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userListDropdown.setAdapter(adapter);


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    datePickerDialog = new DatePickerDialog(
                            CreateNewTaskActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    endDate = LocalDate.of(year, month, dayOfMonth);
                                    dateButton.setText(endDate.toString());
                                }
                            },
                            LocalDate.now().getYear(),
                            LocalDate.now().getMonthValue(),
                            LocalDate.now().getDayOfMonth()
                    );
                }

                // Show the date picker dialog
                datePickerDialog.show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null || user.equals("empty")) {
                    Toast.makeText(getApplicationContext(), "Please fill user", Toast.LENGTH_LONG).show();
                } else if (endDate == null || endDate.isBefore(startDate)) {
                    Toast.makeText(getApplicationContext(), "Please fill in the \"Task End date\" field " +
                            "(minimum value should be tomorrow)", Toast.LENGTH_LONG).show();
                } else if (title == null || String.valueOf(title.getText()).trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill title", Toast.LENGTH_LONG).show();
                } else if (description == null || String.valueOf(description.getText()).trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill description", Toast.LENGTH_LONG).show();
                }
                TaskDto task = new TaskDto();
                task.setUsername(username);
                task.setTitle(String.valueOf(title.getText()));
                task.setDescription(String.valueOf(description.getText()));
                task.setStartDate(startDate.toString());
                task.setEndDate(endDate.toString());
                task.setStatusId(1L);
                userApi.createTask(HttpService.token, task)
                        .enqueue(new Callback<MessageResponse>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse( Call<MessageResponse> call,  Response<MessageResponse> response) {
                                assert response.body() != null;
                                if (response.body().getMessage() == null){
                                    Toast.makeText(CreateNewTaskActivity.this, "Task created", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CreateNewTaskActivity.this, AdminHomeActivity.class));
                                }else {
                                    Toast.makeText(CreateNewTaskActivity.this, "Create is failed", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<MessageResponse> call, Throwable t) {
                                Toast.makeText(CreateNewTaskActivity.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                                Logger.getLogger(CreateNewTaskActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                            }
                        });
            }

        });

        userListDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user = parent.getItemAtPosition(position).toString();
                username = position == 0 ? "empty" : userList.get(position).substring(userList.get(position).lastIndexOf("(")+1,userList.get(position).lastIndexOf(")"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateNewTaskActivity.this, LoginActivity.class));
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateNewTaskActivity.this, AdminHomeActivity.class));
            }
        });
    }

    private void initializeComponents() {

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.getAllUsers(HttpService.token)
                .enqueue(new Callback<List<User>>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        assert response.body() != null;
                        for (User user : response.body()) {
                            userList.add(user.getFirstName() + " " + user.getLastName() + " (" + user.getUserName() + ")");
                        }

                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(CreateNewTaskActivity.this, "Page loading failed", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(UserHomeActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }
}