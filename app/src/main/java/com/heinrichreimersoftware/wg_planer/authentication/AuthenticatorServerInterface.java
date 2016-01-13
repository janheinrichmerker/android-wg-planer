package com.heinrichreimersoftware.wg_planer.authentication;

import com.google.gson.Gson;
import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.structure.User;
import com.heinrichreimersoftware.wg_planer.sync.api.WgService;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthenticatorServerInterface {

    public static String login(String username) throws IOException {
        Gson gson = new Gson();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .build();

        WgService api = retrofit.create(WgService.class);

        Response<ResponseBody> response = api.getUser(username).execute();
        if (response.isSuccess()) {
            return gson.fromJson(response.body().charStream(), User.class).getUsername();
        }
        return null;
    }
}