import React from "react";

import { _t } from '../../languageHandler';
import { MatrixClientPeg } from "../../MatrixClientPeg";
import BaseCard from "../views/right_panel/BaseCard";
import { replaceableComponent } from "../../utils/replaceableComponent";
import TimelinePanel from "./TimelinePanel";
import Spinner from "../views/elements/Spinner";
import { TileShape } from "../views/rooms/EventTile";
import { Layout } from "../../settings/Layout";

interface IProps {
    onClose(): void;
}

/*
 * Component which shows the global notification list using a TimelinePanel
 */
@replaceableComponent("structures.NotificationPanel")
export default class NotificationPanel extends React.PureComponent<IProps> {
    render() {
        const emptyState = (<div className="mx_RightPanel_empty mx_NotificationPanel_empty">
            <h2>{ _t('You’re all caught up') }</h2>
            <p>{ _t('You have no visible notifications.') }</p>
        </div>);

        let content;
        const timelineSet = MatrixClientPeg.get().getNotifTimelineSet();
        if (timelineSet) {
            // wrap a TimelinePanel with the jump-to-event bits turned off.
            content = (
                <TimelinePanel
                    manageReadReceipts={false}
                    manageReadMarkers={false}
                    timelineSet={timelineSet}
                    showUrlPreview={false}
                    tileShape={TileShape.Notif}
                    empty={emptyState}
                    alwaysShowTimestamps={true}
                    layout={Layout.Group}
                />
            );
        } else {
            console.error("No notifTimelineSet available!");
            content = <Spinner />;
        }

        return <BaseCard className="mx_NotificationPanel" onClose={this.props.onClose} withoutScrollContainer>
            { content }
        </BaseCard>;
    }
}
