package com.heinrichreimersoftware.wg_planer.dashclock;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.data.RepresentationsContentHelper;
import com.heinrichreimersoftware.wg_planer.data.RepresentationsContract;
import com.heinrichreimersoftware.wg_planer.data.UserContentHelper;
import com.heinrichreimersoftware.wg_planer.structure.LessonTimeFactory;
import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.structure.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DashClockExtensionRepresentations extends DashClockExtension {

    //FIXME

    @Override
    protected void onInitialize(boolean isReconnect) {
        Log.d(MainActivity.TAG, "Initializing Dashclock extension for new representations");
        super.onInitialize(isReconnect);
        addWatchContentUris(new String[]{RepresentationsContract.CONTENT_URI.toString()});
    }

    @Override
    protected void onUpdateData(int reason) {
        Log.d(MainActivity.TAG, "Updating Dashclock extension for new representations");

        Context context = getApplicationContext();

        if (context != null) {
            User user = UserContentHelper.getUser(context);

            List<Representation> allRepresentations;
            if (user != null && !user.getSchoolClass().equals("")) {
                allRepresentations = RepresentationsContentHelper.getRepresentationsFuture(context, user.getSchoolClass());
            } else {
                allRepresentations = RepresentationsContentHelper.getRepresentationsFuture(context);
            }

            Calendar currentTime = new GregorianCalendar();

            List<Representation> representations = new ArrayList<>();
            for (Representation representation : allRepresentations) {
                Calendar representationEndTime = representation.getDate();
                Calendar time = LessonTimeFactory.fromRepresentation(representation).endTime;
                representationEndTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
                representationEndTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
                representationEndTime.set(Calendar.SECOND, time.get(Calendar.SECOND));
                representationEndTime.add(Calendar.MINUTE, 5);
                representationEndTime.add(Calendar.HOUR, -1);
                if (representationEndTime.after(currentTime)) {
                    representations.add(representation);
                }
            }

            int representationsCount = representations.size();

            if (representationsCount > 0) {
                int icon;
                String status;
                String expandedTitle;
                String expandedBody;


                if (representationsCount > 1) {
                    icon = R.drawable.ic_notification_representations;
                    status = String.valueOf(representationsCount);
                    expandedTitle = context.getResources().getQuantityString(R.plurals.label_representations_count, representationsCount, representationsCount);

                    expandedBody = "";
                    boolean first = true;
                    for (Representation representation : representations) {
                        if (!first) {
                            expandedBody += ", ";
                        }
                        expandedBody += representation.getRepresentedSubjectText();
                        first = false;
                    }
                } else {
                    Representation representation = representations.get(0);

                    status = representation.getRepresentedSubject().getShorthand();
                    icon = R.drawable.ic_notification_representation;
                    expandedTitle = representation.getSummary();
                    expandedBody = representation.getDescription(context);
                }

                Intent clickIntent = new Intent(context, MainActivity.class);
                clickIntent.putExtra(MainActivity.INTENT_EXTRA_SELECTED_ITEM, MainActivity.DRAWER_ID_REPRESENTATIONS);

                publishUpdate(new ExtensionData()
                        .visible(true)
                        .icon(icon)
                        .status(status)
                        .expandedTitle(expandedTitle)
                        .expandedBody(expandedBody)
                        .clickIntent(clickIntent));

                Log.d(MainActivity.TAG, representationsCount + " representations. Notifying");
            } else {
                publishUpdate(new ExtensionData().visible(false));
                Log.d(MainActivity.TAG, "No representations. Cancelling notification");
            }
        }
    }
}