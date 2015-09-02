package com.heinrichreimersoftware.wg_planer.geo;

import com.google.android.gms.location.Geofence;

public class WgCoordinates {

    private static final String REQUEST_ID_MAIN_BUILDING = "WG_MAIN_BUILDING";
    private static final String REQUEST_ID_BRANCH_OFFICE = "WG_BRANCH_OFFICE";

    private static final long EXPIRATION_DURATION = 1000 * 60 * 5; // 5 min

    public static Geofence getMainBuildingGeofence() {
        return new Geofence.Builder()
                .setRequestId(REQUEST_ID_MAIN_BUILDING)
                        //.setCircularRegion(52.260495, 10.534015, 75)
                .setCircularRegion(52.219134, 10.513496, 5) // Fake 1: Turmfalkenweg 19
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setExpirationDuration(EXPIRATION_DURATION)
                .build();
    }

    public static Geofence getBranchOfficeGeofence() {
        return new Geofence.Builder()
                .setRequestId(REQUEST_ID_BRANCH_OFFICE)
                        //.setCircularRegion(52.260013, 10.537085, 30)
                .setCircularRegion(52.218853, 10.511849, 5) // Fake 2: Sperberweg 2
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setExpirationDuration(EXPIRATION_DURATION)
                .build();
    }

    private static class CircularRegion {
        double latitude;
        double longitude;
        float radius;

        CircularRegion(double latitude, double longitude, float radius) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.radius = radius;
        }
    }

}
