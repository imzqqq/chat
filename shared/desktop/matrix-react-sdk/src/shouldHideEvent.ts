import { MatrixEvent } from "matrix-js-sdk/src/models/event";

import SettingsStore from "./settings/SettingsStore";
import { IState } from "./components/structures/RoomView";

interface IDiff {
    isMemberEvent: boolean;
    isJoin?: boolean;
    isPart?: boolean;
    isDisplaynameChange?: boolean;
    isAvatarChange?: boolean;
}

function memberEventDiff(ev: MatrixEvent): IDiff {
    const diff: IDiff = {
        isMemberEvent: ev.getType() === 'm.room.member',
    };

    // If is not a Member Event then the other checks do not apply, so bail early.
    if (!diff.isMemberEvent) return diff;

    const content = ev.getContent();
    const prevContent = ev.getPrevContent();

    const isMembershipChanged = content.membership !== prevContent.membership;
    diff.isJoin = isMembershipChanged && content.membership === 'join';
    diff.isPart = isMembershipChanged && content.membership === 'leave' && ev.getStateKey() === ev.getSender();

    const isJoinToJoin = !isMembershipChanged && content.membership === 'join';
    diff.isDisplaynameChange = isJoinToJoin && content.displayname !== prevContent.displayname;
    diff.isAvatarChange = isJoinToJoin && content.avatar_url !== prevContent.avatar_url;
    return diff;
}

/**
 * Determines whether the given event should be hidden from timelines.
 * @param ev The event
 * @param ctx An optional RoomContext to pull cached settings values from to avoid
 *     hitting the settings store
 */
export default function shouldHideEvent(ev: MatrixEvent, ctx?: IState): boolean {
    // Accessing the settings store directly can be expensive if done frequently,
    // so we should prefer using cached values if a RoomContext is available
    const isEnabled = ctx ?
        name => ctx[name] :
        name => SettingsStore.getValue(name, ev.getRoomId());

    // Hide redacted events
    if (ev.isRedacted() && !isEnabled('showRedactions')) return true;

    // Hide replacement events since they update the original tile (if enabled)
    if (ev.isRelation("m.replace")) return true;

    const eventDiff = memberEventDiff(ev);

    if (eventDiff.isMemberEvent) {
        if ((eventDiff.isJoin || eventDiff.isPart) && !isEnabled('showJoinLeaves')) return true;
        if (eventDiff.isAvatarChange && !isEnabled('showAvatarChanges')) return true;
        if (eventDiff.isDisplaynameChange && !isEnabled('showDisplaynameChanges')) return true;
    }

    return false;
}
