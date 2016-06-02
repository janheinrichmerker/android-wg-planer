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
import com.heinrichreimersoftware.wg_planer.utils.Utils;

public class RepresentationsSyncAdapter extends AbstractThreadedSyncAdapter {

    public RepresentationsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public RepresentationsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Representation[] oldRepresentations = RepresentationsContentHelper.getRepresentations(getContext());
        Representation[] representations = new SyncServerInterface(getContext()).getRepresentations();

        representations = ClassesUtils.filterRepresentations(getContext(), representations);

        if (representations.length != oldRepresentations.length || !Utils.containsAll(representations, oldRepresentations)) {
            RepresentationsNotification.notify(getContext());
        }

        RepresentationsContentHelper.clearRepresentations(getContext());

        if (representations.length > 0) {
            RepresentationsContentHelper.addRepresentations(getContext(), representations);
        }
    }
}
