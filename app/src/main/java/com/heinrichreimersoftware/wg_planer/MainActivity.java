package com.heinrichreimersoftware.wg_planer;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.wg_planer.data.RepresentationsContentHelper;
import com.heinrichreimersoftware.wg_planer.data.RepresentationsContract;
import com.heinrichreimersoftware.wg_planer.data.UserContentHelper;
import com.heinrichreimersoftware.wg_planer.geo.GeofenceHelper;
import com.heinrichreimersoftware.wg_planer.geo.WgCoordinates;
import com.heinrichreimersoftware.wg_planer.structure.User;
import com.heinrichreimersoftware.wg_planer.sync.SyncStatusManager;
import com.heinrichreimersoftware.wg_planer.utils.SyncUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends DrawerActivity {

    public static final String TAG = "WG-Planer";

    public static final String PREFERENCES_LOGIN = "LoginPreferences";
    public static final String PREFERENCES_COOKIE = "CookiePrefsFile";
    public static final String PREFERENCES_SETTINGS_HAS_SET_DEFAULT = "_has_set_default_values";

    public static final String INTENT_EXTRA_SELECTED_ITEM = "SELECTED_ITEM";

    public static final int NOTIFICATION_LIGHTS_COLOR = Color.GREEN;
    public static final int NOTIFICATION_LIGHTS_ON_MS = 1500;
    public static final int NOTIFICATION_LIGHTS_OFF_MS = 2500;

    public static final int DRAWER_ID_REPRESENTATIONS = 1;
    public static final int DRAWER_ID_TIMETABLE = 2;
    public static final int DRAWER_ID_TEACHERS = 3;
    public static final int DRAWER_ID_SETTINGS = 4;

    private static final String STATE_SLECTED_ITEM_ID = "state_selected_item_id";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private long selectedItemId = DRAWER_ID_TIMETABLE;
    private SyncStatusManager syncStatusManager;
    private RepresentationNavigationFragment fragmentRepresentations = new RepresentationNavigationFragment();
    private TimetableNavigationFragment fragmentTimetable = new TimetableNavigationFragment();
    private TeacherFragment fragmentTeachers = new TeacherFragment();
    private GeofenceHelper geofenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MainActivity.TAG, "monCreate(" + savedInstanceState + ")");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SyncUtils.initialize(this);

        geofenceHelper = new GeofenceHelper(
                this,
                WgCoordinates.getMainBuildingGeofence(),
                WgCoordinates.getBranchOfficeGeofence()
        );
        geofenceHelper.connect();
        geofenceHelper.startMonitoring();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(new ActivityManager.TaskDescription(
                            getString(R.string.title_app),
                            BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher),
                            getResources().getColor(R.color.material_green_600))
            );
        }

        updateDrawerList();

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_SLECTED_ITEM_ID)) {
            selectedItemId = savedInstanceState.getLong(STATE_SLECTED_ITEM_ID, DRAWER_ID_TIMETABLE);
        }

        select(selectedItemId);

        syncStatusManager = new SyncStatusManager(this, RepresentationsContract.AUTHORITY);
        syncStatusManager.addOnSyncStatusChangedListener(new SyncStatusManager.OnSyncStatusChangedListener() {
            @Override
            public void onStatusChanged(int state, String affectedAuthority) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDrawerList();
                    }
                });
            }
        });
    }

    private void updateDrawerList() {
        final User user = UserContentHelper.getUser(this);

        int representationCount;

        if (user != null) {
            String schoolClass = user.getSchoolClass();
            if (schoolClass != null && !schoolClass.equals("")) {
                representationCount = RepresentationsContentHelper.getRepresentationsFuture(this, schoolClass).size();
            } else {
                representationCount = RepresentationsContentHelper.getRepresentationsFuture(this).size();
            }
        } else {
            representationCount = RepresentationsContentHelper.getRepresentationsFuture(this).size();
        }

        clearProfiles();
        if (user != null) {
            DrawerProfile profile = new DrawerProfile()
                    .setBackground(getResources().getDrawable(R.drawable.bg_wg))
                    .setName(user.getName())
                    .setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
                        @Override
                        public void onClick(DrawerProfile drawerProfile, long id) {
                            //TODO open user info dialog instead
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse("file://" + user.getBitmapPath()), "image/*");
                            startActivity(intent);
                        }
                    });
            Bitmap avatar = user.getBitmap();
            if (avatar != null) {
                profile.setRoundedAvatar(this, avatar);
            }
            addProfile(profile);
        }

        clearItems();
        addItem(new DrawerItem()
                        .setId(DRAWER_ID_REPRESENTATIONS)
                        .setImage(getResources().getDrawable(R.drawable.ic_representations))
                        .setTextPrimary(getString(R.string.title_fragment_representation))
                        .setTextSecondary(getResources().getQuantityString(R.plurals.label_representations_count, representationCount, representationCount))
        );
        addItem(new DrawerItem()
                        .setId(DRAWER_ID_TIMETABLE)
                        .setImage(getResources().getDrawable(R.drawable.ic_timetable))
                        .setTextPrimary(getString(R.string.title_fragment_timetable))
        );
        addItem(new DrawerItem()
                        .setId(DRAWER_ID_TEACHERS)
                        .setImage(getResources().getDrawable(R.drawable.ic_teachers))
                        .setTextPrimary(getString(R.string.title_fragment_teacher))
        );
        addDivider();
        addItem(new DrawerItem()
                        .setId(DRAWER_ID_SETTINGS)
                        .setImage(getResources().getDrawable(R.drawable.ic_settings))
                        .setTextPrimary(getString(R.string.title_activity_settings))
        );
        setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            @Override
            public void onClick(DrawerItem drawerItem, long id, int position) {
                select(id);
            }
        });
    }

    private void select(long id) {
        if (id == DRAWER_ID_SETTINGS) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return;
        }
        selectedItemId = id;
        selectItemById(id);
        switch ((int) id) {
            case DRAWER_ID_REPRESENTATIONS:
                switchFragment(fragmentRepresentations);
                toolbar.setVisibility(View.GONE);
                break;
            case DRAWER_ID_TIMETABLE:
                switchFragment(fragmentTimetable);
                toolbar.setVisibility(View.GONE);
                break;
            case DRAWER_ID_TEACHERS:
                switchFragment(fragmentTeachers);
                toolbar.setVisibility(View.VISIBLE);
                setSupportActionBar(toolbar);
                break;
            case DRAWER_ID_SETTINGS:
                switchFragment(fragmentTeachers);
                toolbar.setVisibility(View.VISIBLE);
                setSupportActionBar(toolbar);
                break;
        }
        closeDrawer();
    }

    /**
     * Swaps fragments in the main content view
     */
    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.content) == fragment) return;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SyncUtils.initialize(this);
        updateDrawerList();

        syncStatusManager.start();
    }

    @Override
    protected void onPause() {
        syncStatusManager.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        geofenceHelper.disconnect();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_SLECTED_ITEM_ID, selectedItemId);
        Log.d(MainActivity.TAG, "monSaveInstanceState(" + outState + ")");
        super.onSaveInstanceState(outState);
    }
}


