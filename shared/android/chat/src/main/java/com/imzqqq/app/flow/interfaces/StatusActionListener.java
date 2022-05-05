package com.imzqqq.app.flow.interfaces;

import android.view.View;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface StatusActionListener extends LinkListener {
    void onReply(int position);
    void onReblog(final boolean reblog, final int position);
    void onFavourite(final boolean favourite, final int position);
    void onBookmark(final boolean bookmark, final int position);
    void onMore(@NonNull View view, final int position);
    void onViewMedia(int position, int attachmentIndex, @Nullable View view);
    void onViewThread(int position);

    /**
     * Open reblog author for the status.
     * @param position At which position in the list status is located
     */
    void onOpenReblog(int position);
    void onExpandedChange(boolean expanded, int position);
    void onContentHiddenChange(boolean isShowing, int position);
    void  onLoadMore(int position);

    /**
     * Called when the status {@link android.widget.ToggleButton} responsible for collapsing long
     * status content is interacted with.
     *
     * @param isCollapsed Whether the status content is shown in a collapsed state or fully.
     * @param position    The position of the status in the list.
     */
    void onContentCollapsedChange(boolean isCollapsed, int position);

    /**
     * called when the reblog count has been clicked
     * @param position The position of the status in the list.
     */
    default void onShowReblogs(int position) {}

    /**
     * called when the favourite count has been clicked
     * @param position The position of the status in the list.
     */
    default void onShowFavs(int position) {}

    void onVoteInPoll(int position, @NonNull List<Integer> choices);

}
