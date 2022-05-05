import React from "react";
import { SearchResult } from "matrix-js-sdk/src/models/search-result";
import RoomContext from "../../../contexts/RoomContext";
import SettingsStore from "../../../settings/SettingsStore";
import { UIFeature } from "../../../settings/UIFeature";
import { RoomPermalinkCreator } from '../../../utils/permalinks/Permalinks';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import DateSeparator from "../messages/DateSeparator";
import EventTile, { haveTileForEvent } from "./EventTile";
import { Layout } from "../../../settings/Layout";
import { UserNameColorMode } from "../../../settings/UserNameColorMode";

interface IProps {
    // a matrix-js-sdk SearchResult containing the details of this result
    searchResult: SearchResult;
    // a list of strings to be highlighted in the results
    searchHighlights?: string[];
    // href for the highlights in this result
    resultLink?: string;
    onHeightChanged?: () => void;
    permalinkCreator?: RoomPermalinkCreator;
    layout?: Layout;
    singleSideBubbles?: boolean;
    userNameColorMode?: UserNameColorMode;
}

@replaceableComponent("views.rooms.SearchResultTile")
export default class SearchResultTile extends React.Component<IProps> {
    static contextType = RoomContext;

    public render() {
        const result = this.props.searchResult;
        const mxEv = result.context.getEvent();
        const eventId = mxEv.getId();

        const ts1 = mxEv.getTs();
        const ret = [<DateSeparator key={ts1 + "-search"} ts={ts1} />];
        const isTwelveHour = SettingsStore.getValue("showTwelveHourTimestamps");
        const alwaysShowTimestamps = SettingsStore.getValue("alwaysShowTimestamps");
        const enableFlair = SettingsStore.getValue(UIFeature.Flair);

        const timeline = result.context.getTimeline();
        for (let j = 0; j < timeline.length; j++) {
            const ev = timeline[j];
            let highlights;
            const contextual = (j != result.context.getOurEventIndex());
            if (!contextual) {
                highlights = this.props.searchHighlights;
            }
            if (haveTileForEvent(ev, this.context?.showHiddenEventsInTimeline)) {
                ret.push(
                    <EventTile
                        key={`${eventId}+${j}`}
                        mxEvent={ev}
                        layout={this.props.layout}
                        singleSideBubbles={this.props.singleSideBubbles}
                        userNameColorMode={this.props.userNameColorMode}
                        contextual={contextual}
                        highlights={highlights}
                        permalinkCreator={this.props.permalinkCreator}
                        highlightLink={this.props.resultLink}
                        onHeightChanged={this.props.onHeightChanged}
                        isTwelveHour={isTwelveHour}
                        alwaysShowTimestamps={alwaysShowTimestamps}
                        enableFlair={enableFlair}
                    />,
                );
            }
        }

        return <li data-scroll-tokens={eventId}>{ ret }</li>;
    }
}
