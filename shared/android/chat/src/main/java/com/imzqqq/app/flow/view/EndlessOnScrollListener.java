package com.imzqqq.app.flow.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener {
    private static final int VISIBLE_THRESHOLD = 15;
    private int previousTotalItemCount;
    private LinearLayoutManager layoutManager;

    public EndlessOnScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        previousTotalItemCount = 0;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView view, int dx, int dy) {
        int totalItemCount = layoutManager.getItemCount();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        if (totalItemCount < previousTotalItemCount) {
            previousTotalItemCount = totalItemCount;

        }
        if (totalItemCount != previousTotalItemCount) {
            previousTotalItemCount = totalItemCount;
        }

        if (lastVisibleItemPosition + VISIBLE_THRESHOLD > totalItemCount) {
            onLoadMore(totalItemCount, view);
        }
    }

    public void reset() {
        previousTotalItemCount = 0;
    }

    public abstract void onLoadMore(int totalItemsCount, RecyclerView view);
}
