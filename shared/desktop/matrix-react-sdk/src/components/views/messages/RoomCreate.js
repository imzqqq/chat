import React from 'react';
import PropTypes from 'prop-types';

import dis from '../../../dispatcher/dispatcher';
import { RoomPermalinkCreator } from '../../../utils/permalinks/Permalinks';
import { _t } from '../../../languageHandler';
import { MatrixClientPeg } from '../../../MatrixClientPeg';
import EventTileBubble from "./EventTileBubble";
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.messages.RoomCreate")
export default class RoomCreate extends React.Component {
    static propTypes = {
        /* the MatrixEvent to show */
        mxEvent: PropTypes.object.isRequired,
    };

    _onLinkClicked = e => {
        e.preventDefault();

        const predecessor = this.props.mxEvent.getContent()['predecessor'];

        dis.dispatch({
            action: 'view_room',
            event_id: predecessor['event_id'],
            highlighted: true,
            room_id: predecessor['room_id'],
        });
    };

    render() {
        const predecessor = this.props.mxEvent.getContent()['predecessor'];
        if (predecessor === undefined) {
            return <div />; // We should never have been instantiated in this case
        }
        const prevRoom = MatrixClientPeg.get().getRoom(predecessor['room_id']);
        const permalinkCreator = new RoomPermalinkCreator(prevRoom, predecessor['room_id']);
        permalinkCreator.load();
        const predecessorPermalink = permalinkCreator.forEvent(predecessor['event_id']);
        const link = (
            <a href={predecessorPermalink} onClick={this._onLinkClicked}>
                { _t("Click here to see older messages.") }
            </a>
        );

        return <EventTileBubble
            className="mx_CreateEvent"
            title={_t("This room is a continuation of another conversation.")}
            subtitle={link}
        />;
    }
}
