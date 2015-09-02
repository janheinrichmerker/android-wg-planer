package com.heinrichreimersoftware.wg_planer.structure;

import android.util.Base64;

import com.google.gson.Gson;

public class AuthToken {
    private String username;
    private String password;

    public AuthToken(String base64) {
        fromBase64(base64);
    }

    public AuthToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toJson() {
        Gson gson = new Gson();
        String json = gson.toJson(this, AuthToken.class);
        return json;
    }

    public void fromJson(String json) {
        Gson gson = new Gson();
        AuthToken obj = gson.fromJson(json, AuthToken.class);
        this.password = obj.password;
        this.username = obj.username;
    }

    public String toString() {
        return this.toBase64();
    }

    public String toBase64() {
        String base64 = Base64.encodeToString(this.toJson().getBytes(), Base64.DEFAULT);
        return base64;
    }

    public void fromBase64(String base64) {
        try {
            byte[] data = Base64.decode(base64, Base64.DEFAULT);
            String json = new String(data);
            if (!json.equals("")) {
                this.fromJson(json);
            }
        } catch (IllegalArgumentException ignored) {
        }
    }
}