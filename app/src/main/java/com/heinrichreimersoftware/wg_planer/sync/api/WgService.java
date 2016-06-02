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
    Call<User> getUser(@Path("username") String username); //TODO Change API to return the user directly (not binding it to "user" key)

    @GET("/teachers/")
    Call<Teacher[]> getTeachers(); //TODO Change API to return the array of teachers directly (not binding it to "teachers" key)

    @GET("/timetable/")
    Call<Lesson[]> getTimetable(); //TODO Change API to return the array of lessons directly (not binding it to "timetable" key)

    @GET("/timetable/{schoolClasses}/")
    Call<Lesson[]> getTimetable(@Path("schoolClasses") String schoolClasses); //TODO Change API to return the array of lessons directly (not binding it to "timetable" key)

    @GET("/representations/")
    Call<Representation[]> getRepresentations(); //TODO Change API to return the array of representations directly (not binding it to "representations" key)

    @GET("/representations/{schoolClasses}/")
    Call<Representation[]> getRepresentations(@Path("schoolClasses") String schoolClasses); //TODO Change API to return the array of representations directly (not binding it to "representations" key)
}
