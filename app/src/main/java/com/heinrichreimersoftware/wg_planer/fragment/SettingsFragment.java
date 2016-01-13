package com.heinrichreimersoftware.wg_planer.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.heinrichreimersoftware.wg_planer.ClassesActivity;
import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.SettingsActivity;
import com.heinrichreimersoftware.wg_planer.authentication.AccountGeneral;
import com.heinrichreimersoftware.wg_planer.dialog.InfoDialogBuilder;
import com.heinrichreimersoftware.wg_planer.utils.SyncUtils;

import de.psdev.licensesdialog.LicensesDialog;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().setTaskDescription(new ActivityManager.TaskDescription(
                            getString(R.string.title_activity_settings),
                            BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher),
                            getResources().getColor(R.color.material_green_600))
            );
        }

        PreferenceManager.setDefaultValues(getActivity(), R.xml.prefs, false);

        Preference classesListPreference = findPreference(getString(R.string.key_preference_classes_list_click));
        if (classesListPreference != null) {
            classesListPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), ClassesActivity.class);
                    startActivity(intent);
                    return false;
                }
            });
        }

        Preference updateUserInfoPreference = findPreference(getString(R.string.key_preference_update_user_info));
        if (updateUserInfoPreference != null) {
            updateUserInfoPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

                    AccountManager accountManager = AccountManager.get(getActivity());
                    if (accountManager != null) {
                        for (Account account : accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)) {
                            ContentResolver.requestSync(account, Constants.CONTENT_AUTHORITY_USERS, bundle);
                        }
                    }
                    return false;
                }
            });
        }

        Preference logoutPreference = findPreference(getString(R.string.key_preference_logout));
        if (logoutPreference != null) {
            logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.title_dialog_confirm_logout)
                            .content(R.string.text_dialog_confirm_logout)
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
            infoPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new InfoDialogBuilder(getActivity()).show();
                    return false;
                }
            });
        }

        Preference licensesPreference = findPreference(getString(R.string.key_preference_licenses));
        if (licensesPreference != null) {
            licensesPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new LicensesDialog.Builder(getActivity())
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

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
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

    @SuppressWarnings("deprecation")
    private void logout() {
        AccountManager accountManager = AccountManager.get(getActivity());
        if (accountManager != null) {
            Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
            if (accounts.length > 0) {
                final Account account = accounts[0];

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    accountManager.removeAccount(account, getActivity(), new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                if (future.getResult().containsKey(AccountManager.KEY_BOOLEAN_RESULT) && future.getResult().getBoolean(AccountManager.KEY_BOOLEAN_RESULT)) {
                                    Toast.makeText(getActivity(), getString(R.string.text_logout_success, account.name), Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                    return;
                                }
                                Toast.makeText(getActivity(), R.string.text_logout_not_allowed, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), R.string.text_logout_failed, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, null);
                } else {
                    accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
                        @Override
                        public void run(AccountManagerFuture<Boolean> future) {
                            try {
                                if (future.getResult()) {
                                    Toast.makeText(getActivity(), getString(R.string.text_logout_success, account.name), Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                    return;
                                }
                                Toast.makeText(getActivity(), R.string.text_logout_not_allowed, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), R.string.text_logout_failed, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, null);
                }
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_preference_automatic_sync))) {
            SyncUtils.initialize(getActivity());
        } else if (key.equals(getString(R.string.key_preference_geo_notifications))) {
            ((SettingsActivity) getActivity()).updateGeofences();
        }
    }
}
