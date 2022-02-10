package com.imzqqq.app.flow.interfaces;

import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public interface ActionButtonActivity {

    /* Return the ActionButton of the Activity to hide or show it on scroll */
    @Nullable
    FloatingActionButton getActionButton();
}
