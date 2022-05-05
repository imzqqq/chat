import React from "react";
import { IRecordingUpdate, VoiceRecording } from "../../../audio/VoiceRecording";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import Clock from "./Clock";
import { MarkedExecution } from "../../../utils/MarkedExecution";

interface IProps {
    recorder: VoiceRecording;
}

interface IState {
    seconds: number;
}

/**
 * A clock for a live recording.
 */
@replaceableComponent("views.audio_messages.LiveRecordingClock")
export default class LiveRecordingClock extends React.PureComponent<IProps, IState> {
    private seconds = 0;
    private scheduledUpdate = new MarkedExecution(
        () => this.updateClock(),
        () => requestAnimationFrame(() => this.scheduledUpdate.trigger()),
    );

    constructor(props) {
        super(props);
        this.state = {
            seconds: 0,
        };
    }

    componentDidMount() {
        this.props.recorder.liveData.onUpdate((update: IRecordingUpdate) => {
            this.seconds = update.timeSeconds;
            this.scheduledUpdate.mark();
        });
    }

    private updateClock() {
        this.setState({
            seconds: this.seconds,
        });
    }

    public render() {
        return <Clock seconds={this.state.seconds} />;
    }
}
