import React from "react";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { arraySeed, arrayTrimFill } from "../../../utils/arrays";
import Waveform from "./Waveform";
import { Playback, PLAYBACK_WAVEFORM_SAMPLES } from "../../../audio/Playback";
import { percentageOf } from "../../../utils/numbers";

interface IProps {
    playback: Playback;
}

interface IState {
    heights: number[];
    progress: number;
}

/**
 * A waveform which shows the waveform of a previously recorded recording
 */
@replaceableComponent("views.audio_messages.PlaybackWaveform")
export default class PlaybackWaveform extends React.PureComponent<IProps, IState> {
    public constructor(props) {
        super(props);

        this.state = {
            heights: this.toHeights(this.props.playback.waveform),
            progress: 0, // default no progress
        };

        this.props.playback.waveformData.onUpdate(this.onWaveformUpdate);
        this.props.playback.clockInfo.liveData.onUpdate(this.onTimeUpdate);
    }

    private toHeights(waveform: number[]) {
        const seed = arraySeed(0, PLAYBACK_WAVEFORM_SAMPLES);
        return arrayTrimFill(waveform, PLAYBACK_WAVEFORM_SAMPLES, seed);
    }

    private onWaveformUpdate = (waveform: number[]) => {
        this.setState({ heights: this.toHeights(waveform) });
    };

    private onTimeUpdate = (time: number[]) => {
        // Track percentages to a general precision to avoid over-waking the component.
        const progress = Number(percentageOf(time[0], 0, time[1]).toFixed(3));
        this.setState({ progress });
    };

    public render() {
        return <Waveform relHeights={this.state.heights} progress={this.state.progress} />;
    }
}
