package com.imzqqq.app.core.preference;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.Preference;

public abstract class ScPreferenceFragment extends PreferenceFragmentCompat {

    private static final String DIALOG_FRAGMENT_TAG =
            "com.imzqqq.app.core.preference.ScPreferenceFragment.DIALOG";

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // check if dialog is already showing
        if (getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
            return;
        }
        if (preference instanceof ColorMatrixListPreference) {
                ColorMatrixListPreferenceDialogFragment dialogFragment =
                        ColorMatrixListPreferenceDialogFragment.newInstance(preference.getKey());
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

}
