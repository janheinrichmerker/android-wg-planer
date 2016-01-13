package com.heinrichreimersoftware.wg_planer;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.location.Geofence;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;
import com.heinrichreimersoftware.wg_planer.content.RepresentationsContentHelper;
import com.heinrichreimersoftware.wg_planer.content.UserContentHelper;
import com.heinrichreimersoftware.wg_planer.fragment.RepresentationNavigationFragment;
import com.heinrichreimersoftware.wg_planer.fragment.TeacherFragment;
import com.heinrichreimersoftware.wg_planer.fragment.TimetableNavigationFragment;
import com.heinrichreimersoftware.wg_planer.geo.GeofenceActivityHelper;
import com.heinrichreimersoftware.wg_planer.geo.GeofenceHelper;
import com.heinrichreimersoftware.wg_planer.geo.GeofenceTransitionsIntentService;
import com.heinrichreimersoftware.wg_planer.geo.WgCoordinates;
import com.heinrichreimersoftware.wg_planer.structure.User;
import com.heinrichreimersoftware.wg_planer.sync.SyncStatusManager;
import com.heinrichreimersoftware.wg_planer.utils.SyncUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends DrawerActivity {

    public static final String TAG = "WG-Planer";

    public static final int DRAWER_ID_REPRESENTATIONS = 1;
    public static final int DRAWER_ID_TIMETABLE = 2;
    public static final int DRAWER_ID_TEACHERS = 3;
    public static final int DRAWER_ID_SETTINGS = 4;

    public static final String EXTRA_SELECTED_ITEM = "EXTRA_SELECTED_ITEM";
    private static final String STATE_SELECTED_ITEM = "STATE_SELECTED_ITEM";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private long selectedItemId = -1;
    private SyncStatusManager syncStatusManager;
    private RepresentationNavigationFragment fragmentRepresentations = new RepresentationNavigationFragment();
    private TimetableNavigationFragment fragmentTimetable = new TimetableNavigationFragment();
    private TeacherFragment fragmentTeachers = new TeacherFragment();
    private GeofenceActivityHelper geofenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SyncUtils.initialize(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(new ActivityManager.TaskDescription(
                            getString(R.string.title_app),
                            BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher),
                            getResources().getColor(R.color.material_green_600))
            );
        }

        updateDrawerList();
        setDrawerTheme(new DrawerTheme(this)
                        .setStatusBarBackgroundColorRes(android.R.color.transparent)
        );

        long id = DRAWER_ID_TIMETABLE;
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_SELECTED_ITEM)) {
            id = savedInstanceState.getLong(STATE_SELECTED_ITEM, DRAWER_ID_TIMETABLE);
        }
        select(id);

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

        List<Geofence> geofences = new ArrayList<>();
        geofences.add(WgCoordinates.getMainBuildingGeofence());
        geofences.add(WgCoordinates.getBranchOfficeGeofence());

        geofenceHelper = new GeofenceActivityHelper(
                this,
                geofences,
                GeofenceTransitionsIntentService.class,
                GeofenceHelper.INITIAL_TRIGGER_ENTER | GeofenceHelper.INITIAL_TRIGGER_EXIT);
        geofenceHelper.update();
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
                    .setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_wg, getTheme()))
                    .setName(user.getName());
            Bitmap avatar = user.getBitmap();
            if (avatar != null) {
                profile.setRoundedAvatar(this, avatar);
            }
            addProfile(profile);
        }

        clearItems();
        addItem(new DrawerItem()
                        .setId(DRAWER_ID_REPRESENTATIONS)
                        .setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_representations, getTheme()))
                        .setTextPrimary(getString(R.string.title_fragment_representation))
                        .setTextSecondary(getResources().getQuantityString(R.plurals.label_representations_count, representationCount, representationCount))
        );
        addItem(new DrawerItem()
                        .setId(DRAWER_ID_TIMETABLE)
                        .setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_timetable, getTheme()))
                        .setTextPrimary(getString(R.string.title_fragment_timetable))
        );
        addItem(new DrawerItem()
                        .setId(DRAWER_ID_TEACHERS)
                        .setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_teachers, getTheme()))
                        .setTextPrimary(getString(R.string.title_fragment_teacher))
        );
        addDivider();
        addItem(new DrawerItem()
                        .setId(DRAWER_ID_SETTINGS)
                        .setImage(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_settings, getTheme()))
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
        if (id == selectedItemId) {
            switchFragment(getSupportFragmentManager().findFragmentById(R.id.content));
        }
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

        Fragment oldFragment = fragmentManager.findFragmentById(R.id.content);

        if (oldFragment != null && fragment != null && oldFragment.getClass().getName().equals(fragment.getClass().getName()))
            return;

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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_SELECTED_ITEM, selectedItemId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        geofenceHelper.checkPermission(requestCode, permissions, grantResults);
    }
}


