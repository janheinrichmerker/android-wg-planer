package com.heinrichreimersoftware.wg_planer.sync.api;

import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.structure.Teacher;
import com.heinrichreimersoftware.wg_planer.structure.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WgService {

    @GET("/users/{username}/")
    Call<User> getUser(@Path("username") String username);

    @GET("/teachers/")
    Call<Teacher[]> getTeachers();

    @GET("/timetable/")
    Call<Lesson[]> getTimetable();

    @GET("/timetable/{schoolClasses}/")
    Call<Lesson[]> getTimetable(@Path("schoolClasses") String schoolClasses);

    @GET("/representations/")
    Call<Representation[]> getRepresentations();

    @GET("/representations/{schoolClasses}/")
    Call<Representation[]> getRepresentations(@Path("schoolClasses") String schoolClasses);
}
