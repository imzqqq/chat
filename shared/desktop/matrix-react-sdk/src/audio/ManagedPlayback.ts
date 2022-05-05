import { DEFAULT_WAVEFORM, Playback } from "./Playback";
import { PlaybackManager } from "./PlaybackManager";

/**
 * A managed playback is a Playback instance that is guided by a PlaybackManager.
 */
export class ManagedPlayback extends Playback {
    public constructor(private manager: PlaybackManager, buf: ArrayBuffer, seedWaveform = DEFAULT_WAVEFORM) {
        super(buf, seedWaveform);
    }

    public async play(): Promise<void> {
        this.manager.pauseAllExcept(this);
        return super.play();
    }

    public destroy() {
        this.manager.destroyPlaybackInstance(this);
        super.destroy();
    }
}
