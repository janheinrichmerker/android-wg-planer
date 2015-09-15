package com.heinrichreimersoftware.wg_planer.dialog;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.data.UserContentHelper;
import com.heinrichreimersoftware.wg_planer.structure.User;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InfoDialogBuilder extends MaterialDialog.Builder {

    @Bind(R.id.version)
    TextView version;
    @Bind(R.id.copyright)
    TextView copyright;
    @Bind(R.id.userInfo)
    TextView userInfo;

    public InfoDialogBuilder(final Context context) {
        super(context);

        positiveText(R.string.action_close);
        customView(R.layout.dialog_view_info, true);

        ButterKnife.bind(this, customView);

        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException ignored) {
        }
        if (!versionName.equals("")) {
            version.setText(context.getResources().getString(R.string.label_dialog_info_version, versionName));
        } else {
            version.setVisibility(View.GONE);
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        copyright.setText(context.getResources().getString(R.string.label_dialog_info_copyright, year));

        User user = UserContentHelper.getUser(context);

        String username = getContext().getString(R.string.label_unknown);
        String schoolClass = getContext().getString(R.string.label_unknown);

        if (user != null && user.getUsername() != null && !user.getUsername().equals("")) {
            username = user.getUsername();
        }
        if (user != null && user.getSchoolClass() != null && !user.getSchoolClass().equals("")) {
            schoolClass = user.getSchoolClass();
        }

        userInfo.setText(context.getResources().getString(R.string.label_dialog_info_user, username, schoolClass));
    }
}