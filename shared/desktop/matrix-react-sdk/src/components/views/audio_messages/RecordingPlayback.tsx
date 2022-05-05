import React, { ReactNode } from "react";
import PlayPauseButton from "./PlayPauseButton";
import PlaybackClock from "./PlaybackClock";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { TileShape } from "../rooms/EventTile";
import PlaybackWaveform from "./PlaybackWaveform";
import AudioPlayerBase from "./AudioPlayerBase";

@replaceableComponent("views.audio_messages.RecordingPlayback")
export default class RecordingPlayback extends AudioPlayerBase {
    private get isWaveformable(): boolean {
        return this.props.tileShape !== TileShape.Notif
            && this.props.tileShape !== TileShape.FileGrid
            && this.props.tileShape !== TileShape.Pinned;
    }

    protected renderComponent(): ReactNode {
        const shapeClass = !this.isWaveformable ? 'mx_VoiceMessagePrimaryContainer_noWaveform' : '';
        return (
            <div className={'mx_MediaBody mx_VoiceMessagePrimaryContainer ' + shapeClass}>
                <PlayPauseButton playback={this.props.playback} playbackPhase={this.state.playbackPhase} />
                <PlaybackClock playback={this.props.playback} />
                { this.isWaveformable && <PlaybackWaveform playback={this.props.playback} /> }
            </div>
        );
    }
}
