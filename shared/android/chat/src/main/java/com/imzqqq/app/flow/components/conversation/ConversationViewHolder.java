package com.imzqqq.app.flow.components.conversation;

import android.content.Context;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.imzqqq.app.R;
import com.imzqqq.app.flow.adapter.StatusBaseViewHolder;
import com.imzqqq.app.flow.entity.Attachment;

import com.imzqqq.app.flow.interfaces.StatusActionListener;
import com.imzqqq.app.flow.util.ImageLoadingHelper;
import com.imzqqq.app.flow.util.SmartLengthInputFilter;
import com.imzqqq.app.flow.util.StatusDisplayOptions;
import com.imzqqq.app.flow.viewdata.PollViewDataKt;

import java.util.List;

public class ConversationViewHolder extends StatusBaseViewHolder {

    private static final InputFilter[] COLLAPSE_INPUT_FILTER = new InputFilter[]{SmartLengthInputFilter.INSTANCE};
    private static final InputFilter[] NO_INPUT_FILTER = new InputFilter[0];

    private final TextView conversationNameTextView;
    private final Button contentCollapseButton;
    private final ImageView[] avatars;

    private final StatusDisplayOptions statusDisplayOptions;
    private final StatusActionListener listener;

    ConversationViewHolder(View itemView,
                           StatusDisplayOptions statusDisplayOptions,
                           StatusActionListener listener) {
        super(itemView);
        conversationNameTextView = itemView.findViewById(R.id.conversation_name);
        contentCollapseButton = itemView.findViewById(R.id.button_toggle_content);
        avatars = new ImageView[]{
                avatar,
                itemView.findViewById(R.id.status_avatar_1),
                itemView.findViewById(R.id.status_avatar_2)
        };
        this.statusDisplayOptions = statusDisplayOptions;

        this.listener = listener;

    }

    @Override
    protected int getMediaPreviewHeight(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.status_media_preview_height);
    }

    void setupWithConversation(ConversationEntity conversation) {
        ConversationStatusEntity status = conversation.getLastStatus();
        ConversationAccountEntity account = status.getAccount();

        setupCollapsedState(status.getCollapsible(), status.getCollapsed(),
                status.getExpanded(), status.getSpoilerText(), listener);

        setDisplayName(account.getDisplayName(), account.getEmojis(), statusDisplayOptions);
        setUsername(account.getUsername());
        setCreatedAt(status.getCreatedAt(), statusDisplayOptions);
        setIsReply(status.getInReplyToId() != null);
        setFavourited(status.getFavourited());
        setBookmarked(status.getBookmarked());
        List<Attachment> attachments = status.getAttachments();
        boolean sensitive = status.getSensitive();
        if (statusDisplayOptions.mediaPreviewEnabled() && hasPreviewableAttachment(attachments)) {
            setMediaPreviews(attachments, sensitive, listener, status.getShowingHiddenContent(),
                    statusDisplayOptions.useBlurhash());

            if (attachments.size() == 0) {
                hideSensitiveMediaWarning();
            }
            // Hide the unused label.
            for (TextView mediaLabel : mediaLabels) {
                mediaLabel.setVisibility(View.GONE);
            }
        } else {
            setMediaLabel(attachments, sensitive, listener, status.getShowingHiddenContent());
            // Hide all unused views.
            mediaPreviews[0].setVisibility(View.GONE);
            mediaPreviews[1].setVisibility(View.GONE);
            mediaPreviews[2].setVisibility(View.GONE);
            mediaPreviews[3].setVisibility(View.GONE);
            hideSensitiveMediaWarning();
        }

        setupButtons(listener, account.getId(), status.getContent().toString(),
                statusDisplayOptions);

        setSpoilerAndContent(status.getExpanded(), status.getContent(), status.getSpoilerText(),
                status.getMentions(), status.getEmojis(),
                PollViewDataKt.toViewData(status.getPoll()), statusDisplayOptions, listener);

        setConversationName(conversation.getAccounts());

        setAvatars(conversation.getAccounts());
    }

    private void setConversationName(List<ConversationAccountEntity> accounts) {
        Context context = conversationNameTextView.getContext();
        String conversationName = "";
        if (accounts.size() == 1) {
            conversationName = context.getString(R.string.conversation_1_recipients, accounts.get(0).getUsername());
        } else if (accounts.size() == 2) {
            conversationName = context.getString(R.string.conversation_2_recipients, accounts.get(0).getUsername(), accounts.get(1).getUsername());
        } else if (accounts.size() > 2) {
            conversationName = context.getString(R.string.conversation_more_recipients, accounts.get(0).getUsername(), accounts.get(1).getUsername(), accounts.size() - 2);
        }

        conversationNameTextView.setText(conversationName);
    }

    private void setAvatars(List<ConversationAccountEntity> accounts) {
        for (int i = 0; i < avatars.length; i++) {
            ImageView avatarView = avatars[i];
            if (i < accounts.size()) {
                ImageLoadingHelper.loadAvatar(accounts.get(i).getAvatar(), avatarView,
                        avatarRadius48dp, statusDisplayOptions.animateAvatars());
                avatarView.setVisibility(View.VISIBLE);
            } else {
                avatarView.setVisibility(View.GONE);
            }
        }
    }

    private void setupCollapsedState(boolean collapsible, boolean collapsed, boolean expanded,
                                     String spoilerText, final StatusActionListener listener) {
        /* input filter for TextViews have to be set before text */
        if (collapsible && (expanded || TextUtils.isEmpty(spoilerText))) {
            contentCollapseButton.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                    listener.onContentCollapsedChange(!collapsed, position);
            });

            contentCollapseButton.setVisibility(View.VISIBLE);
            if (collapsed) {
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