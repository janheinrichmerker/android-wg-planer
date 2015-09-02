package com.heinrichreimersoftware.wg_planer.authentication;

import android.content.Context;

public interface ServerAuthenticate {
    public String userSignIn(final String user, final String pass, String authType, Context context) throws Exception;
}