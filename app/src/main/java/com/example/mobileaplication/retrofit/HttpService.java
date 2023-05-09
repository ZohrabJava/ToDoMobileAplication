package com.example.mobileaplication.retrofit;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpService {

    private static final OkHttpClient httpClient = new OkHttpClient();
    public static String username;
    public static String token;


    public static String sendPost(String url, Map<String, String> params) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody formBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            token = response.headers().get("Authorization");

            if (token !=null && url.contains("/login")){
                JwtTokenParser.JwtPayload jwtPayload = JwtTokenParser.parseJwt(token.substring(token.indexOf(" ")).trim());
                username = jwtPayload.sub;
                return Arrays.asList(jwtPayload.roles).toString();
            }
            return response.body().string();
        }
    }
}
