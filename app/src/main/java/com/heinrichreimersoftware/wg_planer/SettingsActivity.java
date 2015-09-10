package com.heinrichreimersoftware.wg_planer;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.heinrichreimersoftware.wg_planer.activity.AppCompatPreferenceActivity;
import com.heinrichreimersoftware.wg_planer.authentication.AccountGeneral;
import com.heinrichreimersoftware.wg_planer.data.UserContract;
import com.heinrichreimersoftware.wg_planer.dialog.InfoDialogBuilder;
import com.heinrichreimersoftware.wg_planer.utils.SyncUtils;

import de.psdev.licensesdialog.LicensesDialog;

public class SettingsActivity extends AppCompatPreferenceActivity implements Preference.OnPreferenceChangeListener, OnSharedPreferenceChangeListener {

    //TODO replace with inlined PreferenceFragment

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(new ActivityManager.TaskDescription(
                            getString(R.string.title_activity_settings),
                            BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher),
                            getResources().getColor(R.color.material_green_600))
            );
        }

        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);

        Preference classesListPreference = findPreference(getString(R.string.key_preference_classes_list_click));
        if (classesListPreference != null) {
            classesListPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(SettingsActivity.this, ClassesActivity.class);
                    startActivity(intent);
                    return false;
                }
            });
        }

        Preference updateUserInfoPreference = findPreference(getString(R.string.key_preference_update_user_info));
        if (updateUserInfoPreference != null) {
            updateUserInfoPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

                    AccountManager accountManager = AccountManager.get(SettingsActivity.this);
                    if (accountManager != null) {
                        for (Account account : accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)) {
                            ContentResolver.requestSync(account, UserContract.AUTHORITY, bundle);
                        }
                    }
                    return false;
                }
            });
        }

        Preference logoutPreference = findPreference(getString(R.string.key_preference_logout));
        if (logoutPreference != null) {
            logoutPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new MaterialDialog.Builder(SettingsActivity.this)
                            .title(R.string.title_dialog_logout)
                            .content(R.string.text_dialog_logout)
                            .positiveText(android.R.string.yes)
                            .negativeText(android.R.string.cancel)
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    logout();
                                }
                            })
                            .show();
                    return false;
                }
            });
        }

        Preference infoPreference = findPreference(getString(R.string.key_preference_info));
        if (infoPreference != null) {
            infoPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new InfoDialogBuilder(SettingsActivity.this).show();
                    return false;
                }
            });
        }

        Preference licensesPreference = findPreference(getString(R.string.key_preference_licenses));
        if (licensesPreference != null) {
            licensesPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new LicensesDialog.Builder(SettingsActivity.this)
                            .setNotices(R.raw.notices)
                            .setCloseText(R.string.action_close)
                            .setDividerColorId(R.color.divider)
                            .build()
                            .show();
                    return false;
                }
            });
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_preference_automatic_sync))) {
            SyncUtils.initialize(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        PreferenceManager preferenceManager = getPreferenceManager();
        if (preferenceManager != null) {
            SharedPreferences sharedPreferences = preferenceManager.getSharedPreferences();
            if (sharedPreferences != null) {
                sharedPreferences.registerOnSharedPreferenceChangeListener(this);
            }
        }
    }

    @Override
    public void onPause() {
        PreferenceManager preferenceManager = getPreferenceManager();
        if (preferenceManager != null) {
            SharedPreferences sharedPreferences = preferenceManager.getSharedPreferences();
            if (sharedPreferences != null) {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
            }
        }
        super.onPause();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) NavUtils.navigateUpFromSameTask(this);
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AccountManager accountManager = AccountManager.get(this);
        if (accountManager != null) {
            Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
            if (accounts.length > 0) {
                final Account account = accounts[0];

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    accountManager.removeAccount(account, this, new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                if (future.getResult().containsKey(AccountManager.KEY_BOOLEAN_RESULT) && future.getResult().getBoolean(AccountManager.KEY_BOOLEAN_RESULT)) {
                                    Toast.makeText(SettingsActivity.this, "\"" + account.name + "\" erfolgreich ausgeloggt.", Toast.LENGTH_SHORT).show();
                                    SettingsActivity.this.finish();
                                    return;
                                }
                                Toast.makeText(SettingsActivity.this, "Ausloggen nicht möglich.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(SettingsActivity.this, "Ausloggen fehlgeschlagen.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, null);
                } else {
                    accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
                        @Override
                        public void run(AccountManagerFuture<Boolean> future) {
                            try {
                                if (future.getResult()) {
                                    Toast.makeText(SettingsActivity.this, "\"" + account.name + "\" erfolgreich ausgeloggt.", Toast.LENGTH_SHORT).show();
                                    SettingsActivity.this.finish();
                                    return;
                                }
                                Toast.makeText(SettingsActivity.this, "Ausloggen nicht möglich.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(SettingsActivity.this, "Ausloggen fehlgeschlagen.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, null);
                }
            }
        }
    }
}