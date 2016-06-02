package com.heinrichreimersoftware.wg_planer.authentication;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    public final static String ARG_LOGIN_ERROR = "ARG_LOGIN_ERROR";
    public final static String ARG_LOGIN_ERROR_TEXT = "ARG_LOGIN_ERROR_TEXT";
    public final static String ARG_LOGIN_USERNAME = "ARG_LOGIN_USERNAME";

    public final static String LOGIN_EULA_READ = "LOGIN_EULA_READ";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    @BindView(R.id.title) TextView title;
    @BindView(R.id.error) TextView error;
    @BindView(R.id.username) EditText username;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.submit) Button submit;
    @BindView(R.id.progress) ProgressBar progress;

    private AccountManager mAccountManager;
    private String mAuthTokenType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BitmapDrawable appIcon = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher, getTheme());
            setTaskDescription(new ActivityManager.TaskDescription(
                            getString(R.string.title_app),
                            (appIcon != null) ? appIcon.getBitmap() : null,
                            ContextCompat.getColor(this, R.color.material_green_600))
            );
        }

        mAccountManager = AccountManager.get(getBaseContext());

        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        if (accounts.length > 0) {
            Toast.makeText(this, "Sie können nur einen Account erstellen.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (sharedPreferences != null && !sharedPreferences.getBoolean(LOGIN_EULA_READ, false)) {
                //EULA must only be read once
                new MaterialDialog.Builder(this)
                        .title(R.string.title_dialog_confirm_privacy)
                        .content(R.string.label_dialog_confirm_privacy)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                sharedPreferences.edit().putBoolean(LOGIN_EULA_READ, true).apply();
                                dialog.cancel();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                finish();
                            }
                        })
                        .show();
            }

            mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
            if (mAuthTokenType == null) {
                mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
            }

            Bundle extras = getIntent().getExtras();

            if (extras != null && extras.getBoolean(ARG_LOGIN_ERROR, false)) {
                error.setVisibility(View.VISIBLE);

                String loginErrorText = extras.getString(ARG_LOGIN_ERROR_TEXT);
                if (loginErrorText != null && !loginErrorText.equals("")) {
                    error.setText(extras.getString(ARG_LOGIN_ERROR_TEXT));
                }

                String loginUsername = extras.getString(ARG_LOGIN_USERNAME);
                if (loginUsername != null && !loginUsername.equals("")) {
                    username.setText(extras.getString(ARG_LOGIN_USERNAME));
                    password.requestFocus();
                } else {
                    username.requestFocus();
                }
            } else {
                username.requestFocus();
            }

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit();
                }
            });
            password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == AuthenticatorActivity.this.getResources().getInteger(R.integer.ime_action_login)) {
                        submit();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQ_SIGN_UP = 1;
        if (requestCode == REQ_SIGN_UP && resultCode == RESULT_OK) {
            finishLogin(data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void submit() {

        error.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        username.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        CharSequence userNameCharSequence = username.getText();
        CharSequence userPassCharSequence = password.getText();

        String userName = "";
        if (userNameCharSequence != null) {
            userName = userNameCharSequence.toString();
        }
        String userPass = "";
        if (userPassCharSequence != null) {
            userPass = userPassCharSequence.toString();
        }

        final String userNameFinal = userName;
        final String userPassFinal = userPass;

        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        if (!userName.equals("") && !userPass.equals("")) {
            final Context context = this;

            new AsyncTask<String, Void, Intent>() {

                @Override
                protected Intent doInBackground(String... params) {

                    Bundle data = new Bundle();
                    try {
                        String authToken;
                        authToken = AuthenticatorServerInterface.login(userNameFinal);

                        data.putString(AccountManager.KEY_ACCOUNT_NAME, userNameFinal);
                        data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                        data.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                        data.putString(PARAM_USER_PASS, userPassFinal);

                    } catch (IOException e) {
                        data.putInt(KEY_ERROR_MESSAGE, R.string.label_login_error_network);
                    }

                    final Intent res = new Intent();
                    res.putExtras(data);
                    return res;
                }

                @Override
                protected void onPostExecute(Intent intent) {
                    String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
                    int errorTextResId = -1;

                    if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                        errorTextResId = intent.getIntExtra(KEY_ERROR_MESSAGE, R.string.label_login_error_unknown);
                    } else if (authToken == null) {
                        errorTextResId = R.string.label_login_error_unknown;
                    } else if (authToken.equals("")) {
                        errorTextResId = R.string.label_login_error_no_return;
                    } else {
                        finishLogin(intent);
                    }

                    if (errorTextResId > 0) {
                        String errorText = getString(errorTextResId);
                        Toast.makeText(getBaseContext(), errorText, Toast.LENGTH_SHORT).show();

                        Intent intentReload = getIntent();
                        overridePendingTransition(0, 0);
                        intentReload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intentReload.putExtra(ARG_LOGIN_ERROR, true);
                        intentReload.putExtra(ARG_LOGIN_ERROR_TEXT, errorText);
                        intentReload.putExtra(ARG_LOGIN_USERNAME, userNameFinal);
                        finish();

                        overridePendingTransition(0, 0);
                        startActivity(intentReload);
                    }
                }
            }.execute();
        } else {
            Intent intentReload = getIntent();
            overridePendingTransition(0, 0);
            intentReload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intentReload.putExtra(ARG_LOGIN_ERROR, true);
            if (!userName.equals("")) {
                intentReload.putExtra(ARG_LOGIN_USERNAME, userName);
            }
            finish();

            overridePendingTransition(0, 0);
            startActivity(intentReload);
        }
    }

    private void finishLogin(Intent intent) {
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authTokenType = mAuthTokenType;

            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authTokenType, authToken);
        } else {
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}