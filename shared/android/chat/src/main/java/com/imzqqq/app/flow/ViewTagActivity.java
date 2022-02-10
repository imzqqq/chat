package com.imzqqq.app.flow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Collections;

import dagger.hilt.android.AndroidEntryPoint;
import com.imzqqq.app.flow.components.timeline.TimelineFragment;

@AndroidEntryPoint
public class ViewTagActivity extends BottomSheetActivity {

    private static final String HASHTAG = "hashtag";

    public static Intent getIntent(Context context, String tag){
        Intent intent = new Intent(context,ViewTagActivity.class);
        intent.putExtra(HASHTAG,tag);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.imzqqq.app.R.layout.activity_view_tag);

        String hashtag = getIntent().getStringExtra(HASHTAG);

        Toolbar toolbar = findViewById(com.imzqqq.app.R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle(String.format(getString(com.imzqqq.app.R.string.title_tag), hashtag));
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = TimelineFragment.newHashtagInstance(Collections.singletonList(hashtag));
        fragmentTransaction.replace(com.imzqqq.app.R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
