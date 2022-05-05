import { DEFAULT_WAVEFORM, Playback, PlaybackState } from "./Playback";
import { ManagedPlayback } from "./ManagedPlayback";

/**
 * Handles management of playback instances to ensure certain functionality, like
 * one playback operating at any one time.
 */
export class PlaybackManager {
    private static internalInstance: PlaybackManager;

    private instances: ManagedPlayback[] = [];

    public static get instance(): PlaybackManager {
        if (!PlaybackManager.internalInstance) {
            PlaybackManager.internalInstance = new PlaybackManager();
        }
        return PlaybackManager.internalInstance;
    }

    /**
     * Pauses all other playback instances. If no playback is provided, all playing
     * instances are paused.
     * @param playback Optional. The playback to leave untouched.
     */
    public pauseAllExcept(playback?: Playback) {
        this.instances
            .filter(p => p !== playback && p.currentState === PlaybackState.Playing)
            .forEach(p => p.pause());
    }

    public destroyPlaybackInstance(playback: ManagedPlayback) {
        this.instances = this.instances.filter(p => p !== playback);
    }

    public createPlaybackInstance(buf: ArrayBuffer, waveform = DEFAULT_WAVEFORM): Playback {
        const instance = new ManagedPlayback(this, buf, waveform);
        this.instances.push(instance);
        return instance;
    }
}
