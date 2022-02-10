package com.imzqqq.app.flow;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.imzqqq.app.R;
import com.imzqqq.app.flow.adapter.AccountSelectionAdapter;
import com.imzqqq.app.flow.db.AccountEntity;
import com.imzqqq.app.flow.db.AccountManager;
import com.imzqqq.app.flow.interfaces.AccountSelectionListener;
import com.imzqqq.app.flow.interfaces.PermissionRequester;

public abstract class BaseActivity extends AppCompatActivity {

    @Inject public AccountManager accountManager;

    private static final int REQUESTER_NONE = Integer.MAX_VALUE;
    private HashMap<Integer, PermissionRequester> requesters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* There isn't presently a way to globally change the theme of a whole application at
         * runtime, just individual activities. So, each activity has to set its theme before any
         * views are created. */
        /* MARK - imzqqq
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = preferences.getString("appTheme", ThemeUtils.APP_THEME_DEFAULT);
        Timber.d(theme);
        if (theme.equals("black")) {
            setTheme(com.imzqqq.app.R.style.FlowBlackTheme);
        }

        // set the taskdescription programmatically, the theme would turn it blue
        String appName = getString(R.string.app_flow_name);
        Bitmap appIcon = BitmapFactory.decodeResource(getResources(), com.imzqqq.app.R.mipmap.ic_launcher);
        int recentsBackgroundColor = ThemeUtils.getColor(this, com.imzqqq.app.R.attr.colorSurface);

        setTaskDescription(new ActivityManager.TaskDescription(appName, appIcon, recentsBackgroundColor));

        int style = textStyle(preferences.getString("statusTextSize", "medium"));
        getTheme().applyStyle(style, false);*/

        if(requiresLogin()) {
            redirectIfNotLoggedIn();
        }

        requesters = new HashMap<>();
    }

    //@Override
    //protected void attachBaseContext(Context base) {
    //    super.attachBaseContext(VectorApplication.getLocaleManager().setLocale(base));
    //}

    protected boolean requiresLogin() {
        return true;
    }

    private static int textStyle(String name) {
        int style;
        switch (name) {
            case "smallest":
                style = com.imzqqq.app.R.style.TextSizeSmallest;
                break;
            case "small":
                style = com.imzqqq.app.R.style.TextSizeSmall;
                break;
            case "medium":
            default:
                style = com.imzqqq.app.R.style.TextSizeMedium;
                break;
            case "large":
                style = com.imzqqq.app.R.style.TextSizeLarge;
                break;
            case "largest":
                style = com.imzqqq.app.R.style.TextSizeLargest;
                break;
        }
        return style;
    }

    public void startActivityWithSlideInAnimation(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public void finishWithoutSlideOutAnimation() {
        super.finish();
    }

    protected void redirectIfNotLoggedIn() {
        AccountEntity account = accountManager.getActiveAccount();
        if (account == null) {
            Intent intent = new Intent(this, FlowLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityWithSlideInAnimation(intent);
            finish();
        }
    }

    protected void showErrorDialog(View anyView, @StringRes int descriptionId, @StringRes int actionId, View.OnClickListener listener) {
        if (anyView != null) {
            Snackbar bar = Snackbar.make(anyView, getString(descriptionId), Snackbar.LENGTH_SHORT);
            bar.setAction(actionId, listener);
            bar.show();
        }
    }

    public void showAccountChooserDialog(CharSequence dialogTitle, boolean showActiveAccount, AccountSelectionListener listener) {
        List<AccountEntity> accounts = accountManager.getAllAccountsOrderedByActive();
        AccountEntity activeAccount = accountManager.getActiveAccount();

        switch(accounts.size()) {
            case 1:
                listener.onAccountSelected(activeAccount);
                return;
            case 2:
                if (!showActiveAccount) {
                    for (AccountEntity account : accounts) {
                        if (activeAccount != account) {
                            listener.onAccountSelected(account);
                            return;
                        }
                    }
                }
                break;
        }

        if (!showActiveAccount && activeAccount != null) {
            accounts.remove(activeAccount);
        }
        AccountSelectionAdapter adapter = new AccountSelectionAdapter(this);
        adapter.addAll(accounts);

        new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setAdapter(adapter, (dialogInterface, index) -> listener.onAccountSelected(accounts.get(index)))
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requesters.containsKey(requestCode)) {
            PermissionRequester requester = requesters.remove(requestCode);
            requester.onRequestPermissionsResult(permissions, grantResults);
        }
    }

    public void requestPermissions(String[] permissions, PermissionRequester requester) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for(String permission: permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.isEmpty()) {
            int[] permissionsAlreadyGranted = new int[permissions.length];
            for (int i = 0; i < permissionsAlreadyGranted.length; ++i)
                permissionsAlreadyGranted[i] = PackageManager.PERMISSION_GRANTED;
            requester.onRequestPermissionsResult(permissions, permissionsAlreadyGranted);
            return;
        }

        int newKey = requester == null ? REQUESTER_NONE : requesters.size();
        if (newKey != REQUESTER_NONE) {
            requesters.put(newKey, requester);
        }
        String[] permissionsCopy = new String[permissionsToRequest.size()];
        permissionsToRequest.toArray(permissionsCopy);
        ActivityCompat.requestPermissions(this, permissionsCopy, newKey);
    }
}
