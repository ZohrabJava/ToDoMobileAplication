package com.example.mobileaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileaplication.retrofit.HttpService;
import com.example.mobileaplication.retrofit.RetrofitService;
import com.example.mobileaplication.retrofit.UserApi;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText loginUsername, loginPassword;
    MaterialButton bth;
    TextView tv;

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeComponents();


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void initializeComponents() {


        bth = findViewById(R.id.buttonLogin);
        tv = findViewById(R.id.textViewRegistration);


        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        bth.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                // Send POST request in background thread
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        try {
                            loginUsername = findViewById(R.id.editTextLoginUsername);
                            loginPassword = findViewById(R.id.editTextTextLoginPassword);
                            final String username = String.valueOf(loginUsername.getText());
                            final String password = String.valueOf(loginPassword.getText());
//                            String url = "http://172.20.10.2:9090/login";
//                            String url = "http://192.168.88.25:9090/login";
//                            String url = "http://10.5.113.18:9090/login";
                            String url = "http://172.20.10.3:9090/login";
                            Map<String, String> params = new HashMap<>();
                            params.put("username", username);
                            params.put("password", password);
                            return HttpService.sendPost(url, params);
                        } catch (Throwable e) {
//                            Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Error sending POST request", e);
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(String response) {
                        // Handle response from server
                        if (response != null) {
                            if (response.contains("Admin")){
                                startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                            } else if (response.contains("User")) {
                                startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
                            }else {
                                Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                            }
                            Log.i(TAG, "Response from server: " + response);
                        } else {
                            Log.e(TAG, "Error receiving response from server");
                        }
                    }
                }.execute();
            }
        });
    }
}