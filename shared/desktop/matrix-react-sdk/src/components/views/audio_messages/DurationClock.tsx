import React from "react";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import Clock from "./Clock";
import { Playback } from "../../../audio/Playback";

interface IProps {
    playback: Playback;
}

interface IState {
    durationSeconds: number;
}

/**
 * A clock which shows a clip's maximum duration.
 */
@replaceableComponent("views.audio_messages.DurationClock")
export default class DurationClock extends React.PureComponent<IProps, IState> {
    public constructor(props) {
        super(props);

        this.state = {
            // we track the duration on state because we won't really know what the clip duration
            // is until the first time update, and as a PureComponent we are trying to dedupe state
            // updates as much as possible. This is just the easiest way to avoid a forceUpdate() or
            // member property to track "did we get a duration".
            durationSeconds: this.props.playback.clockInfo.durationSeconds,
        };
        this.props.playback.clockInfo.liveData.onUpdate(this.onTimeUpdate);
    }

    private onTimeUpdate = (time: number[]) => {
        this.setState({ durationSeconds: time[1] });
    };

    public render() {
        return <Clock seconds={this.state.durationSeconds} />;
    }
}
