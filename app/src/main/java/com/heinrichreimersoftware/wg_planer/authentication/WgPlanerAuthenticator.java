package com.heinrichreimersoftware.wg_planer.authentication;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.data.DbHelper;

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;
import static com.heinrichreimersoftware.wg_planer.authentication.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
import static com.heinrichreimersoftware.wg_planer.authentication.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;

public class WgPlanerAuthenticator extends AbstractAccountAuthenticator {

    private final Context context;

    public WgPlanerAuthenticator(Context context) {
        super(context);

        this.context = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {

        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        if (accounts.length > 0) {
            Toast.makeText(context, "Sie können nur einen Account erstellen.", Toast.LENGTH_LONG).show();

            Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, null);
            return bundle;
        }

        Intent intent = new Intent(context, AuthenticatorActivity.class);
        intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(AuthenticatorActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        if (!authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return result;
        }

        final AccountManager am = AccountManager.get(context);

        String authToken = am.peekAuthToken(account, authTokenType);

        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        final Intent intent = new Intent(context, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_NAME, account.name);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }


    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (AUTHTOKEN_TYPE_FULL_ACCESS.equals(authTokenType))
            return AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
        else
            return authTokenType + " (Label)";
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(KEY_BOOLEAN_RESULT, false);
        return result;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }


    @NonNull
    @Override
    public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account) throws NetworkErrorException {
        Bundle result = super.getAccountRemovalAllowed(response, account);

        if (!result.containsKey(AccountManager.KEY_INTENT) && result.containsKey(AccountManager.KEY_BOOLEAN_RESULT) && result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)) {
            Log.d("WG-Planer", "Clear user data...");
            Editor loginSettings = context.getSharedPreferences(Constants.PREFERENCES_LOGIN, 0).edit();
            loginSettings.clear();
            loginSettings.apply();

            Log.d("WG-Planer", "Clear cookie data...");
            Editor cookieSettings = context.getSharedPreferences(Constants.PREFERENCES_COOKIE, 0).edit();
            cookieSettings.clear();
            cookieSettings.apply();

            Log.d("WG-Planer", "Clear settings data...");
            Editor settings = PreferenceManager.getDefaultSharedPreferences(context).edit();
            settings.clear();
            settings.apply();
            Editor settingsHasSetDefault = context.getSharedPreferences(Constants.PREFERENCES_KEY_HAS_SET_DEFAULT, 0).edit();
            settingsHasSetDefault.clear();
            settingsHasSetDefault.apply();

            Log.d("WG-Planer", "Clear database...");
            context.deleteDatabase(DbHelper.DATABASE_NAME);
            /*
            RepresentationsContentHelper.clearRepresentations(context);
            TimetableContentHelper.clearTimetable(context);
            TeachersContentHelper.clearTeachers(context);
            UserContentHelper.clearUsers(context);
            //*/
        }

        Log.d(MainActivity.TAG, "getAccountRemovalAllowed(): " + result.toString());

        return result;
    }
}