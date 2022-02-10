package com.imzqqq.app.flow.view;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * override BezelImageView from MaterialDrawer library to provide custom outline
 */
public class BezelImageView extends com.mikepenz.materialdrawer.view.BezelImageView {
    public BezelImageView(Context context) {
        this(context, null);
    }

    public BezelImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezelImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int old_w, int old_h) {
        setOutlineProvider(new CustomOutline(w, h));
    }

    private static class CustomOutline extends ViewOutlineProvider {

        int width;
        int height;

        CustomOutline(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, width, height, width < height ? width / 8f : height / 8f);
        }
    }
}
