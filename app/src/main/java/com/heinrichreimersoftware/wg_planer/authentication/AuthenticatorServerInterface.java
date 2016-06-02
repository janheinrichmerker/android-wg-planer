package com.heinrichreimersoftware.wg_planer.authentication;

import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.structure.User;
import com.heinrichreimersoftware.wg_planer.sync.api.WgService;

import java.io.IOException;

import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthenticatorServerInterface {

    public static String login(String username) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WgService api = retrofit.create(WgService.class);

        Response<User> response = api.getUser(username).execute();
        if (response.isSuccess()) {
            return response.body().getUsername();
        }
        return null;
    }
}