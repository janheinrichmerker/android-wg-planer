package com.heinrichreimersoftware.wg_planer.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.heinrichreimersoftware.wg_planer.content.RepresentationsContentHelper;
import com.heinrichreimersoftware.wg_planer.notifications.RepresentationsNotification;
import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.utils.ClassesUtils;

import java.util.Arrays;
import java.util.List;

public class RepresentationsSyncAdapter extends AbstractThreadedSyncAdapter {

    public RepresentationsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public RepresentationsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        List<Representation> oldRepresentations = Arrays.asList(RepresentationsContentHelper.getRepresentations(getContext()));

        SyncServerInterface serverInterface = new SyncServerInterface(getContext());
        List<Representation> representations = serverInterface.getRepresentations();

        representations = ClassesUtils.filterRepresentations(getContext(), representations);

        if (representations.size() != oldRepresentations.size() || !representations.containsAll(oldRepresentations)) {
            RepresentationsNotification.notify(getContext());
        }

        RepresentationsContentHelper.clearRepresentations(getContext());

        if (representations.size() > 0) {
            RepresentationsContentHelper.addRepresentations(getContext(), representations.toArray(new Representation[representations.size()]));
        }
    }
}
