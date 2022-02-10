import React from "react";
import { MatrixCall } from "matrix-js-sdk/src/webrtc/call";
import { CallFeed } from "matrix-js-sdk/src/webrtc/callFeed";
import VideoFeed from "./VideoFeed";
import classNames from "classnames";

interface IProps {
    feeds: Array<CallFeed>;
    call: MatrixCall;
    pipMode: boolean;
}

export default class CallViewSidebar extends React.Component<IProps> {
    render() {
        const feeds = this.props.feeds.map((feed) => {
            return (
                <VideoFeed
                    key={feed.stream.id}
                    feed={feed}
                    call={this.props.call}
                    primary={false}
                    pipMode={this.props.pipMode}
                />
            );
        });

        const className = classNames("mx_CallViewSidebar", {
            mx_CallViewSidebar_pipMode: this.props.pipMode,
        });

        return (
            <div className={className}>
                { feeds }
            </div>
        );
    }
}
