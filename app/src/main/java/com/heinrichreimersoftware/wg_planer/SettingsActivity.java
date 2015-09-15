package com.heinrichreimersoftware.wg_planer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.Geofence;
import com.heinrichreimersoftware.wg_planer.geo.GeofenceActivityHelper;
import com.heinrichreimersoftware.wg_planer.geo.GeofenceHelper;
import com.heinrichreimersoftware.wg_planer.geo.GeofenceTransitionsIntentService;
import com.heinrichreimersoftware.wg_planer.geo.WgCoordinates;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private GeofenceActivityHelper geofenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

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

    public void updateGeofences() {
        geofenceHelper.update();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        geofenceHelper.checkPermission(requestCode, permissions, grantResults);
    }
}