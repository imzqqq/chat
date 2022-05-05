import React from 'react';

import { _t } from '../../../languageHandler';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    // number of milliseconds ago this user was last active.
    // zero = unknown
    activeAgo?: number;
    // if true, activeAgo is an approximation and "Now" should
    // be shown instead
    currentlyActive?: boolean;
    // offline, online, etc
    presenceState?: string;
}

@replaceableComponent("views.rooms.PresenceLabel")
export default class PresenceLabel extends React.Component<IProps> {
    static defaultProps = {
        activeAgo: -1,
        presenceState: null,
    };

    // Return duration as a string using appropriate time units
    // XXX: This would be better handled using a culture-aware library, but we don't use one yet.
    private getDuration(time: number): string {
        if (!time) return;
        const t = Math.round(time / 1000);
        const s = t % 60;
        const m = Math.round(t / 60) % 60;
        const h = Math.round(t / (60 * 60)) % 24;
        const d = Math.round(t / (60 * 60 * 24));
        if (t < 60) {
            if (t < 0) {
                return _t("%(duration)ss", { duration: 0 });
            }
            return _t("%(duration)ss", { duration: s });
        }
        if (t < 60 * 60) {
            return _t("%(duration)sm", { duration: m });
        }
        if (t < 24 * 60 * 60) {
            return _t("%(duration)sh", { duration: h });
        }
        return _t("%(duration)sd", { duration: d });
    }

    private getPrettyPresence(presence: string, activeAgo: number, currentlyActive: boolean): string {
        if (!currentlyActive && activeAgo !== undefined && activeAgo > 0) {
            const duration = this.getDuration(activeAgo);
            if (presence === "online") return _t("Online for %(duration)s", { duration: duration });
            if (presence === "unavailable") return _t("Idle for %(duration)s", { duration: duration }); // XXX: is this actually right?
            if (presence === "offline") return _t("Offline for %(duration)s", { duration: duration });
            return _t("Unknown for %(duration)s", { duration: duration });
        } else {
            if (presence === "online") return _t("Online");
            if (presence === "unavailable") return _t("Idle"); // XXX: is this actually right?
            if (presence === "offline") return _t("Offline");
            return _t("Unknown");
        }
    }

    render() {
        return (
            <div className="mx_PresenceLabel">
                { this.getPrettyPresence(this.props.presenceState, this.props.activeAgo, this.props.currentlyActive) }
            </div>
        );
    }
}
