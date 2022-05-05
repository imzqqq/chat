import React, { ReactNode } from "react";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import AccessibleTooltipButton from "../elements/AccessibleTooltipButton";
import { _t } from "../../../languageHandler";
import { Playback, PlaybackState } from "../../../audio/Playback";
import classNames from "classnames";

// omitted props are handled by render function
interface IProps extends Omit<React.ComponentProps<typeof AccessibleTooltipButton>, "title" | "onClick" | "disabled"> {
    // Playback instance to manipulate. Cannot change during the component lifecycle.
    playback: Playback;

    // The playback phase to render. Able to change during the component lifecycle.
    playbackPhase: PlaybackState;
}

/**
 * Displays a play/pause button (activating the play/pause function of the recorder)
 * to be displayed in reference to a recording.
 */
@replaceableComponent("views.audio_messages.PlayPauseButton")
export default class PlayPauseButton extends React.PureComponent<IProps> {
    public constructor(props) {
        super(props);
    }

    private onClick = () => {
        // noinspection JSIgnoredPromiseFromCall
        this.toggleState();
    };

    public async toggleState() {
        await this.props.playback.toggle();
    }

    public render(): ReactNode {
        const { playback, playbackPhase, ...restProps } = this.props;
        const isPlaying = playback.isPlaying;
        const isDisabled = playbackPhase === PlaybackState.Decoding;
        const classes = classNames('mx_PlayPauseButton', {
            'mx_PlayPauseButton_play': !isPlaying,
            'mx_PlayPauseButton_pause': isPlaying,
            'mx_PlayPauseButton_disabled': isDisabled,
        });
        return <AccessibleTooltipButton
            className={classes}
            title={isPlaying ? _t("Pause") : _t("Play")}
            onClick={this.onClick}
            disabled={isDisabled}
            {...restProps}
        />;
    }
}
