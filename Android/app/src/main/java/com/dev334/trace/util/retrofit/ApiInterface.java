package com.dev334.trace.util.retrofit;

import com.dev334.trace.model.ApiResponse;
import com.dev334.trace.model.Attendance;
import com.dev334.trace.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //@Headers("auth_token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MWI3YTNkOGViMjYxYjdjMjJkNGYzNjAiLCJpYXQiOjE2Mzk0MjUxMDF9.iRKfrKeuH26HFyqVHnPSmLgQlFH2KAbSTFm5a2yP4g8")
    @GET("/api/data/users")
    Call<List<User>> getUsers();


    @POST("/api/user/register")
    Call<ApiResponse> registerUser(@Body User user);

    @POST("/api/user/login")
    Call<User> loginUser(@Body User user);

    @POST("/api/user/create")
    Call<ApiResponse> createUser(@Body User user);

    @PUT("/api/user/edit")
    Call<ApiResponse> updateUser(@Body User user);

    @GET("api/user/users/{id}")
    Call<User> getUser(@Path("id") String id);

//    @GET("api/blood/req")
//    Call<List<Blood>> getBloodReq(@Query("location") String location, @Query("blood") String blood);
//
//    @POST("/api/blood/req")
//    Call<ApiResponse> reqBlood(@Body Blood blood);
//
//    @GET("/resource/fced6df9-a360-4e08-8ca0-f283fc74ce15?api-key=579b464db66ec23bdd00000127eaf05b3f0e45765d7131e141b41c0c&format=json&offset=0&limit=100")
//    Call<GovApiResponse> getBloodBank(@Query(value="filters[__district]", encoded = true) String location);
//
//    @POST("api/blood/schedule")
//    Call<ApiResponse> schedule(@Body Schedule schedule);
//
//    @GET("api/admin/schedule")
//    Call<List<Schedule>> getSchedule(@Query("bank_id") String bank_id,@Query("pending") String pending);

    @PATCH("api/admin/schedule/approval")
    Call<ApiResponse> setApproval(@Query("id") String id,@Query("approval") String approval);

    @PATCH("api/admin/schedule/status")
    Call<ApiResponse> setSuccess(@Query("id") String id,@Query("approval") String approval);

//    @GET("api/admin/request")
//    Call<List<Blood>> getAdminRequests(@Query("location")String location);

    @PATCH("api/admin/request/verify")
    Call<ApiResponse> markRequest(@Query("id")String id,@Query("verified")String verified);

    @PATCH("api/blood/remove/schedule")
    Call<ApiResponse> removeSchedule(@Query("user_id") String user_id, @Query("schedule_id") String schedule_id);

    @PATCH("api/blood/remove/request")
    Call<ApiResponse> removeRequest(@Query("user_id") String user_id, @Query("request_id") String request_id);

//    @GET("api/blood/user/req")
//    Call<Blood> getUserRequest(@Query("user_id") String user_id);
//
//    @GET("api/blood/user/schedule")
//    Call<Schedule> getUserSchedule(@Query("user_id") String user_id);

    @DELETE("api/user/delete")
    Call<ApiResponse> deleteUser(@Query("user_id") String user_id);

    @POST("api/admin/upload")
    Call<ApiResponse> uploadPictures(@Body Attendance attendance);
//
//    @PATCH("api/user/password")
//    Call<ApiResponse> changePassword(@Body ChangePasswordModel changePassword);
}
