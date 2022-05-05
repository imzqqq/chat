package com.imzqqq.app.flow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import dagger.hilt.android.AndroidEntryPoint;
import com.imzqqq.app.flow.fragment.ViewThreadFragment;
import com.imzqqq.app.flow.util.LinkHelper;

@AndroidEntryPoint
public class ViewThreadActivity extends BottomSheetActivity {

    public static final int REVEAL_BUTTON_HIDDEN = 1;
    public static final int REVEAL_BUTTON_REVEAL = 2;
    public static final int REVEAL_BUTTON_HIDE = 3;

    public static Intent startIntent(Context context, String id, String url) {
        Intent intent = new Intent(context, ViewThreadActivity.class);
        intent.putExtra(ID_EXTRA, id);
        intent.putExtra(URL_EXTRA, url);
        return intent;
    }

    private static final String ID_EXTRA = "id";
    private static final String URL_EXTRA = "url";
    private static final String FRAGMENT_TAG = "ViewThreadFragment_";

    private int revealButtonState = REVEAL_BUTTON_HIDDEN;

    private ViewThreadFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.imzqqq.app.R.layout.activity_view_thread);

        Toolbar toolbar = findViewById(com.imzqqq.app.R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(com.imzqqq.app.R.string.title_view_thread);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        String id = getIntent().getStringExtra(ID_EXTRA);

        fragment = (ViewThreadFragment)getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG + id);
        if(fragment == null) {
            fragment = ViewThreadFragment.newInstance(id);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(com.imzqqq.app.R.id.fragment_container, fragment, FRAGMENT_TAG + id);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.imzqqq.app.R.menu.view_thread_toolbar, menu);
        MenuItem menuItem = menu.findItem(com.imzqqq.app.R.id.action_reveal);
        menuItem.setVisible(revealButtonState != REVEAL_BUTTON_HIDDEN);
        menuItem.setIcon(revealButtonState == REVEAL_BUTTON_REVEAL ?
        com.imzqqq.app.R.drawable.ic_eye_24dp : com.imzqqq.app.R.drawable.ic_hide_media_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    public void setRevealButtonState(int state) {
        switch (state) {
            case REVEAL_BUTTON_HIDDEN:
            case REVEAL_BUTTON_REVEAL:
            case REVEAL_BUTTON_HIDE:
                this.revealButtonState = state;
                invalidateOptionsMenu();
                break;
            default:
                throw new IllegalArgumentException("Invalid reveal button state: " + state);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == com.imzqqq.app.R.id.action_open_in_web) {
            LinkHelper.openLink(getIntent().getStringExtra(URL_EXTRA), this);
            return true;
        } else if (itemId == com.imzqqq.app.R.id.action_reveal) {
            fragment.onRevealPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
