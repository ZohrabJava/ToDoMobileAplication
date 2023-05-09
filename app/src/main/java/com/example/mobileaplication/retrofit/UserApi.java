package com.example.mobileaplication.retrofit;

import com.example.mobileaplication.UserInfo;
import com.example.mobileaplication.model.MessageResponse;
import com.example.mobileaplication.model.TaskDto;
import com.example.mobileaplication.model.User;
import com.example.mobileaplication.model.UserInfoDto;

import java.util.List;

import kotlin.ParameterName;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    @GET("/user/getAllUsers")
    Call<List<User>> getAllUsers(@Header("Authorization") String token);

    @POST("/user/getUsername")
    Call<User> getUserByUsername(@Header("Authorization") String token, @Body User user);
    @GET("/getUser/{username}")
    Call<User> getUserByUsername2(@Header("Authorization") String token, @Path("username") String username);

    @POST("/user/register")
    Call<User> register(@Body User user);

    @POST("/login")
    Call<MessageResponse> login(@Query("myParam") String myParamValue);

    @POST("/task/create")
    Call<MessageResponse> createTask(@Header("Authorization") String token, @Body TaskDto task);

    @GET("/task/getTasksByUsernameAndStatus/{username}/{status}")
    Call<List<TaskDto>> getTasksByUsernameAndStatus(@Header("Authorization") String token, @Path("username") String username, @Path("status") Long status);

    @GET("/task/getAllTasks")
    Call<List<TaskDto>> getAllTask(@Header("Authorization") String token);

    @GET("/task/getTasksById/{taskId}")
    Call<TaskDto> getTaskById(@Header("Authorization") String token, @Path("taskId") Long taskId);

    @POST("/task/changeTaskStatus")
    Call<MessageResponse> changeTaskStatus(@Header("Authorization") String token, @Body TaskDto task);

    @GET("/user/userInfo/{username}")
    Call<UserInfoDto> getUserInfoByUsername(@Header("Authorization") String token, @Path("username") String username);

    @GET("/user/{id}")
    Call<User> getUsrById(@Header("Authorization") String token, @Path("id") Long id);
}
