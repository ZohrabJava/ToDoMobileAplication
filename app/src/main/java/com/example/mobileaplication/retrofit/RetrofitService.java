package com.example.mobileaplication.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService() {
        initilizeRetrofit();
    }

    private void initilizeRetrofit() {
        retrofit = new Retrofit.Builder()
//                .baseUrl("http://172.20.10.2:9090")
//                .baseUrl("http://192.168.88.25:9090")
//                .baseUrl("http://10.5.113.18:9090")
                .baseUrl("http://172.20.10.3:9090")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public  Retrofit getRetrofit(){
        return  retrofit;
    }
}
