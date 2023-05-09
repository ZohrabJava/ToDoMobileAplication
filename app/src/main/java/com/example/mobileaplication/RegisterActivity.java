package com.example.mobileaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileaplication.model.User;
import com.example.mobileaplication.retrofit.RetrofitService;
import com.example.mobileaplication.retrofit.UserApi;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText regFirstName, regLastName, regUsername, regPassword, regConfirmPassword;
    MaterialButton regButton;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeComponents();

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void initializeComponents() {
        regFirstName = findViewById(R.id.editTextRegisterFirstName);
        regLastName = findViewById(R.id.editTextRegisterLastName);
        regUsername = findViewById(R.id.editTextRegisterUsername);
        regPassword = findViewById(R.id.editTextRegisterPassword);
        regConfirmPassword = findViewById(R.id.editTextRegisterConfirmPassword);
        regButton = findViewById(R.id.buttonRegister);
        tv = findViewById(R.id.textViewLogin);

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);


        regButton.setOnClickListener(view -> {
            String firstName = String.valueOf(regFirstName.getText());
            String lastName = String.valueOf(regLastName.getText());
            String username = String.valueOf(regUsername.getText());
            String password = String.valueOf(regPassword.getText());
            String confirmPassword = String.valueOf(regConfirmPassword.getText());

            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUserName(username);
            user.setPassword(password);
            user.setConfirmPassword(confirmPassword);

            if (firstName.length() == 0
                    || lastName.length() == 0
                    || username.length() == 0
                    || password.length() == 0
                    || confirmPassword.length() == 0
            ) {
                Toast.makeText(getApplicationContext(), "Please fill All details", Toast.LENGTH_LONG).show();
            } else if (username.contains(" ")) {
                Toast.makeText(getApplicationContext(), "Please delete space from username", Toast.LENGTH_LONG).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getApplicationContext(), "Password and Confirm password didn't match", Toast.LENGTH_LONG).show();
            } else if (!isValid(password)) {
                Toast.makeText(getApplicationContext(), "Password must contain at least 8 characters, having letter, digit and special symbol", Toast.LENGTH_LONG).show();
            } else {
                userApi.register(user)
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                Toast.makeText(RegisterActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                                Logger.getLogger(RegisterActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                            }
                        });
            }


        });

    }

    public static boolean isValid(String password) {
        int f1 = 0, f2 = 0, f3 = 0;
        if (password.length() < 8) {
            return false;
        } else {
            for (int i = 0; i < password.length(); i++) {
                if (Character.isLetter(password.charAt(i))) {
                    f1 = 1;
                }
            }
            for (int i = 0; i < password.length(); i++) {
                if (Character.isDigit(password.charAt(i))) {
                    f2 = 1;
                }
            }
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if (c >= 33 && c <= 46 || c == 64) {
                    f3 = 1;
                }
            }
            if (f1 == 1 && f2 == 1 && f3 == 1) {
                return true;
            }
            return false;
        }
    }
}