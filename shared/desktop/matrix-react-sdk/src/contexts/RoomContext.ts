import { createContext } from "react";

import { IState } from "../components/structures/RoomView";
import { Layout } from "../settings/Layout";

const RoomContext = createContext<IState>({
    roomLoading: true,
    peekLoading: false,
    shouldPeek: true,
    membersLoaded: false,
    numUnreadMessages: 0,
    draggingFile: false,
    searching: false,
    guestsCanJoin: false,
    canPeek: false,
    showApps: false,
    isPeeking: false,
    showRightPanel: true,
    joining: false,
    atEndOfLiveTimeline: true,
    atEndOfLiveTimelineInit: false,
    showTopUnreadMessagesBar: false,
    statusBarVisible: false,
    canReact: false,
    canReply: false,
    layout: Layout.Group,
    singleSideBubbles: false,
    adaptiveSideBubbles: false,
    lowBandwidth: false,
    alwaysShowTimestamps: false,
    showTwelveHourTimestamps: false,
    readMarkerInViewThresholdMs: 3000,
    readMarkerOutOfViewThresholdMs: 30000,
    showHiddenEventsInTimeline: false,
    showReadReceipts: true,
    showRedactions: true,
    showJoinLeaves: true,
    showAvatarChanges: true,
    showDisplaynameChanges: true,
    matrixClientIsReady: false,
    dragCounter: 0,
});
RoomContext.displayName = "RoomContext";
export default RoomContext;
