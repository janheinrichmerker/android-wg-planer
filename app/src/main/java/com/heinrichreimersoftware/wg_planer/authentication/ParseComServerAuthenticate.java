package com.heinrichreimersoftware.wg_planer.authentication;

import android.content.Context;
import android.util.Log;

import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.exceptions.Base64Exception;
import com.heinrichreimersoftware.wg_planer.exceptions.NetworkException;
import com.heinrichreimersoftware.wg_planer.exceptions.UnknownUsernameException;
import com.heinrichreimersoftware.wg_planer.exceptions.WrongCredentialsException;
import com.heinrichreimersoftware.wg_planer.exceptions.WrongPasswordException;
import com.heinrichreimersoftware.wg_planer.structure.AuthToken;
import com.heinrichreimersoftware.wg_planer.utils.Utils;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;


public class ParseComServerAuthenticate implements ServerAuthenticate {

    public static String login(Context context, OkHttpClient client, String username, String password) throws NetworkException, UnknownUsernameException, WrongPasswordException, WrongCredentialsException, Base64Exception {

        if (Utils.hasActiveInternetConnection(context)) {


            RequestBody data = new FormEncodingBuilder()
                    .add("login_act", username)
                    .add("login_pwd", password)
                    .build();

            Request request = new Request.Builder()
                    .url(Constants.IDESK_URL)
                    .post(data)
                    .build();

            Response response;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                throw new NetworkException("Negative response");
            }

            if (!response.isSuccessful()) {
                throw new NetworkException("Unexpected code " + response);
            }

            String html;
            try {
                html = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                throw new NetworkException();
            }

            Document doc = Jsoup.parse(html, Constants.IDESK_URL);

            Elements errorElements = doc.select("table.ms > tbody > tr > td.vam > p.err.hac");
            if (errorElements.size() > 1) {
                if (errorElements.get(1).text().equals("Account existiert nicht!")) {
                    throw new UnknownUsernameException();
                } else if (errorElements.get(1).text().equals("Anmeldung fehlgeschlagen!")) {
                    throw new WrongPasswordException();
                } else {
                    throw new WrongCredentialsException();
                }
            } else if (errorElements.size() == 1) {
                throw new WrongCredentialsException("Username or password not entered.");
            } else {
                AuthToken authToken = new AuthToken(username, password);
                String authTokenString = authToken.toBase64();

                if (authTokenString != null && !authTokenString.equals("")) {
                    return authTokenString;
                }
                throw new Base64Exception();
            }

        }
        throw new NetworkException("No connection");
    }

    @Override
    public String userSignIn(String username, String password, String authType, Context context) throws UnknownUsernameException, WrongPasswordException, NetworkException, WrongCredentialsException, Base64Exception {


        Log.d(MainActivity.TAG, "userSignIn: '" + username + "'");

        OkHttpClient client = new OkHttpClient();

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        client.setCookieHandler(cookieManager);

        return login(context, client, username, password);
    }
}