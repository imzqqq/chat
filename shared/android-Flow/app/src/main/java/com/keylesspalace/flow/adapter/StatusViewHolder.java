/* Copyright 2017 Andrew Dawson
 *
 * This file is a part of Flow.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Flow is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Flow; if not,
 * see <http://www.gnu.org/licenses>. */

package com.keylesspalace.flow.adapter;

import android.content.Context;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.keylesspalace.flow.R;
import com.keylesspalace.flow.entity.Emoji;
import com.keylesspalace.flow.entity.Status;
import com.keylesspalace.flow.interfaces.StatusActionListener;
import com.keylesspalace.flow.util.CustomEmojiHelper;
import com.keylesspalace.flow.util.SmartLengthInputFilter;
import com.keylesspalace.flow.util.StatusDisplayOptions;
import com.keylesspalace.flow.util.StringUtils;
import com.keylesspalace.flow.viewdata.StatusViewData;

import java.util.List;

import at.connyduck.sparkbutton.helpers.Utils;

public class StatusViewHolder extends StatusBaseViewHolder {
    private static final InputFilter[] COLLAPSE_INPUT_FILTER = new InputFilter[]{SmartLengthInputFilter.INSTANCE};
    private static final InputFilter[] NO_INPUT_FILTER = new InputFilter[0];

    private TextView statusInfo;
    private Button contentCollapseButton;

    public StatusViewHolder(View itemView) {
        super(itemView);
        statusInfo = itemView.findViewById(R.id.status_info);
        contentCollapseButton = itemView.findViewById(R.id.button_toggle_content);
    }

    @Override
    protected int getMediaPreviewHeight(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.status_media_preview_height);
    }

    @Override
    public void setupWithStatus(StatusViewData.Concrete status,
                                final StatusActionListener listener,
                                StatusDisplayOptions statusDisplayOptions,
                                @Nullable Object payloads) {
        if (payloads == null) {

            setupCollapsedState(status, listener);

            Status reblogging = status.getRebloggingStatus();
            if (reblogging == null) {
                hideStatusInfo();
            } else {
                String rebloggedByDisplayName = reblogging.getAccount().getDisplayName();
                setRebloggedByDisplayName(rebloggedByDisplayName,
                        reblogging.getAccount().getEmojis(), statusDisplayOptions);
                statusInfo.setOnClickListener(v -> listener.onOpenReblog(getBindingAdapterPosition()));
            }

        }
        super.setupWithStatus(status, listener, statusDisplayOptions, payloads);

    }

    private void setRebloggedByDisplayName(final CharSequence name,
                                           final List<Emoji> accountEmoji,
                                           final StatusDisplayOptions statusDisplayOptions) {
        Context context = statusInfo.getContext();
        CharSequence wrappedName = StringUtils.unicodeWrap(name);
        CharSequence boostedText = context.getString(R.string.status_boosted_format, wrappedName);
        CharSequence emojifiedText = CustomEmojiHelper.emojify(
                boostedText, accountEmoji, statusInfo, statusDisplayOptions.animateEmojis()
        );
        statusInfo.setText(emojifiedText);
        statusInfo.setVisibility(View.VISIBLE);
    }

    // don't use this on the same ViewHolder as setRebloggedByDisplayName, will cause recycling issues as paddings are changed
    void setPollInfo(final boolean ownPoll) {
        statusInfo.setText(ownPoll ? R.string.poll_ended_created : R.string.poll_ended_voted);
        statusInfo.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_poll_24dp, 0, 0, 0);
        statusInfo.setCompoundDrawablePadding(Utils.dpToPx(statusInfo.getContext(), 10));
        statusInfo.setPaddingRelative(Utils.dpToPx(statusInfo.getContext(), 28), 0, 0, 0);
        statusInfo.setVisibility(View.VISIBLE);
    }

    void hideStatusInfo() {
        statusInfo.setVisibility(View.GONE);
    }

    private void setupCollapsedState(final StatusViewData.Concrete status, final StatusActionListener listener) {
        /* input filter for TextViews have to be set before text */
        if (status.isCollapsible() && (status.isExpanded() || TextUtils.isEmpty(status.getSpoilerText()))) {
            contentCollapseButton.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                    listener.onContentCollapsedChange(!status.isCollapsed(), position);
            });

            contentCollapseButton.setVisibility(View.VISIBLE);
            if (status.isCollapsed()) {
                contentCollapseButton.setText(R.string.status_content_warning_show_more);
                content.setFilters(COLLAPSE_INPUT_FILTER);
            } else {
                contentCollapseButton.setText(R.string.status_content_warning_show_less);
                content.setFilters(NO_INPUT_FILTER);
            }
        } else {
            contentCollapseButton.setVisibility(View.GONE);
            content.setFilters(NO_INPUT_FILTER);
        }
    }
}
