import React, { ReactNode, useEffect } from 'react';
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { RoomMember } from "matrix-js-sdk/src/models/room-member";

import MemberAvatar from '../avatars/MemberAvatar';
import { _t } from '../../../languageHandler';
import { useStateToggle } from "../../../hooks/useStateToggle";
import AccessibleButton from "./AccessibleButton";
import { Layout } from '../../../settings/Layout';

interface IProps {
    // An array of member events to summarise
    events: MatrixEvent[];
    // The minimum number of events needed to trigger summarisation
    threshold?: number;
    // Whether or not to begin with state.expanded=true
    startExpanded?: boolean;
    // The list of room members for which to show avatars next to the summary
    summaryMembers?: RoomMember[];
    // The text to show as the summary of this event list
    summaryText?: string | JSX.Chat;
    // An array of EventTiles to render when expanded
    children: ReactNode[];
    // Called when the event list expansion is toggled
    onToggle?(): void;
    // The layout currently used
    layout?: Layout;
}

const EventListSummary: React.FC<IProps> = ({
    events,
    children,
    threshold = 3,
    onToggle,
    startExpanded,
    summaryMembers = [],
    summaryText,
    layout,
}) => {
    const [expanded, toggleExpanded] = useStateToggle(startExpanded);

    // Whenever expanded changes call onToggle
    useEffect(() => {
        if (onToggle) {
            onToggle();
        }
    }, [expanded]); // eslint-disable-line react-hooks/exhaustive-deps

    const eventIds = events.map((e) => e.getId()).join(',');

    // If we are only given few events then just pass them through
    if (events.length < threshold) {
        return (
            <li className="mx_EventListSummary" data-scroll-tokens={eventIds} data-expanded={true} data-layout={layout}>
                { children }
            </li>
        );
    }

    let body;
    if (expanded) {
        body = <React.Fragment>
            <div className="mx_EventListSummary_line">&nbsp;</div>
            { children }
        </React.Fragment>;
    } else {
        const avatars = summaryMembers.map((m) => <MemberAvatar key={m.userId} member={m} width={14} height={14} />);
        if (layout === Layout.Bubble) {
            body = (
                <div className="mx_EventTile_line sc_EventTile_bubbleLine sc_EventTile_bubbleLine_info">
                    <div className="sc_EventTile_bubbleArea sc_EventTile_bubbleArea_center sc_EventTile_bubbleArea_info">
                        <div className="sc_EventTile_bubble sc_EventTile_bubble_info sc_EventTile_bubble_center">
                            <span className="mx_EventListSummary_avatars" onClick={toggleExpanded}>
                                { avatars }
                            </span>
                            <span className="mx_TextualEvent mx_EventListSummary_summary">
                                { summaryText }
                            </span>
                        </div>
                    </div>
                </div>
            );
        } else {
            body = (
                <div className="mx_EventTile_line">
                    <div className="mx_EventTile_info">
                        <span className="mx_EventListSummary_avatars" onClick={toggleExpanded}>
                            { avatars }
                        </span>
                        <span className="mx_TextualEvent mx_EventListSummary_summary">
                            { summaryText }
                        </span>
                    </div>
                </div>
            );
        }
    }

    return (
        <li className="mx_EventListSummary" data-scroll-tokens={eventIds} data-expanded={expanded + ""} data-layout={layout}>
            <AccessibleButton className="mx_EventListSummary_toggle" onClick={toggleExpanded} aria-expanded={expanded}>
                { expanded ? _t('collapse') : _t('expand') }
            </AccessibleButton>
            { body }
        </li>
    );
};

EventListSummary.defaultProps = {
    startExpanded: false,
    layout: Layout.Group,
};

export default EventListSummary;
