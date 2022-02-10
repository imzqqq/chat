package com.imzqqq.app.flow.interfaces;

public interface AccountActionListener {
    void onViewAccount(String id);
    void onMute(final boolean mute, final String id, final int position, final boolean notifications);
    void onBlock(final boolean block, final String id, final int position);
    void onRespondToFollowRequest(final boolean accept, final String id, final int position);
}
