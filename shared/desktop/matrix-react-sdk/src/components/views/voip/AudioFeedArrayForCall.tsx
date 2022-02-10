import React from "react";
import AudioFeed from "./AudioFeed";
import { CallEvent, MatrixCall } from "matrix-js-sdk/src/webrtc/call";
import { CallFeed } from "matrix-js-sdk/src/webrtc/callFeed";

interface IProps {
    call: MatrixCall;
}

interface IState {
    feeds: Array<CallFeed>;
}

export default class AudioFeedArrayForCall extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);

        this.state = {
            feeds: this.props.call.getRemoteFeeds(),
        };
    }

    componentDidMount() {
        this.props.call.addListener(CallEvent.FeedsChanged, this.onFeedsChanged);
    }

    componentWillUnmount() {
        this.props.call.removeListener(CallEvent.FeedsChanged, this.onFeedsChanged);
    }

    onFeedsChanged = () => {
        this.setState({
            feeds: this.props.call.getRemoteFeeds(),
        });
    };

    render() {
        return this.state.feeds.map((feed, i) => {
            return (
                <AudioFeed feed={feed} key={i} />
            );
        });
    }
}
