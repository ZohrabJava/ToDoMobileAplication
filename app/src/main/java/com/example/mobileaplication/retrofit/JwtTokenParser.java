package com.example.mobileaplication.retrofit;

import android.util.Base64;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

public class JwtTokenParser {

    private static final Gson gson = new Gson();

    public static JwtPayload parseJwt(String jwt) {
        String[] jwtParts = jwt.split("\\.");
        String encodedPayload = jwtParts[1];
        byte[] decodedBytes = Base64.decode(encodedPayload, Base64.URL_SAFE);
        String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);
        return gson.fromJson(decodedPayload, JwtPayload.class);
    }

    public static class JwtPayload {
        public String sub;
        public String name;
        public String [] roles;
        public long iat;
        public long exp;
        // Add other claims as needed
    }
}
