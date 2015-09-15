package com.heinrichreimersoftware.wg_planer.authentication;

public class AccountGeneral {
    /*Account type id*/
    public static final String ACCOUNT_TYPE = "com.heinrichreimersoftware.wg_planer_auth";

    /*Account name*/
    public static final String ACCOUNT_NAME = "Wilhelm-Gymnasium";

    /*Auth token types*/
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Vollzugriff";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Vollzugriff auf den Wilhelm-Gymnasium Account";

    public static final ServerAuthenticate sServerAuthenticate = new ParseComServerAuthenticate();
}