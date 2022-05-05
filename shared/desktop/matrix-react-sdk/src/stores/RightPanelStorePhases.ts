// These are in their own file because of circular imports being a problem.
export enum RightPanelPhases {
    // Room stuff
    RoomMemberList = 'RoomMemberList',
    FilePanel = 'FilePanel',
    NotificationPanel = 'NotificationPanel',
    RoomMemberInfo = 'RoomMemberInfo',
    EncryptionPanel = 'EncryptionPanel',
    RoomSummary = 'RoomSummary',
    Widget = 'Widget',
    PinnedMessages = "PinnedMessages",

    Room3pidMemberInfo = 'Room3pidMemberInfo',
    // Group stuff
    GroupMemberList = 'GroupMemberList',
    GroupRoomList = 'GroupRoomList',
    GroupRoomInfo = 'GroupRoomInfo',
    GroupMemberInfo = 'GroupMemberInfo',

    // Space stuff
    SpaceMemberList = "SpaceMemberList",
    SpaceMemberInfo = "SpaceMemberInfo",
    Space3pidMemberInfo = "Space3pidMemberInfo",

    // Thread stuff
    ThreadView = "ThreadView",
    ThreadPanel = "ThreadPanel",
}

// These are the phases that are safe to persist (the ones that don't require additional
// arguments).
export const RIGHT_PANEL_PHASES_NO_ARGS = [
    RightPanelPhases.RoomSummary,
    RightPanelPhases.NotificationPanel,
    RightPanelPhases.PinnedMessages,
    RightPanelPhases.FilePanel,
    RightPanelPhases.RoomMemberList,
    RightPanelPhases.GroupMemberList,
    RightPanelPhases.GroupRoomList,
];

// Subset of phases visible in the Space View
export const RIGHT_PANEL_SPACE_PHASES = [
    RightPanelPhases.SpaceMemberList,
    RightPanelPhases.Space3pidMemberInfo,
    RightPanelPhases.SpaceMemberInfo,
];
