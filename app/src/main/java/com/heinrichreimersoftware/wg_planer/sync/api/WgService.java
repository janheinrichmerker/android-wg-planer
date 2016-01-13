package com.heinrichreimersoftware.wg_planer.sync.api;

import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WgService {

    @GET("/users/{username}/")
    Call<ResponseBody> getUser(@Path("username") String username);

    @GET("/teachers/")
    Call<ResponseBody> getTeachers();

    @GET("/timetable/")
    Call<ResponseBody> getTimetable();

    @GET("/timetable/{schoolClasses}/")
    Call<ResponseBody> getTimetable(@Path("schoolClasses") String schoolClasses);

    @GET("/representations/")
    Call<ResponseBody> getRepresentations();

    @GET("/representations/{schoolClasses}/")
    Call<ResponseBody> getRepresentations(@Path("schoolClasses") String schoolClasses);
}
