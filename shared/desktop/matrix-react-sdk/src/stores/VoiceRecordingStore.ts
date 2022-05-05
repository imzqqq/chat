import { AsyncStoreWithClient } from "./AsyncStoreWithClient";
import defaultDispatcher from "../dispatcher/dispatcher";
import { ActionPayload } from "../dispatcher/payloads";
import { VoiceRecording } from "../audio/VoiceRecording";

interface IState {
    recording?: VoiceRecording;
}

export class VoiceRecordingStore extends AsyncStoreWithClient<IState> {
    private static internalInstance: VoiceRecordingStore;

    public constructor() {
        super(defaultDispatcher, {});
    }

    /**
     * Gets the active recording instance, if any.
     */
    public get activeRecording(): VoiceRecording | null {
        return this.state.recording;
    }

    public static get instance(): VoiceRecordingStore {
        if (!VoiceRecordingStore.internalInstance) {
            VoiceRecordingStore.internalInstance = new VoiceRecordingStore();
        }
        return VoiceRecordingStore.internalInstance;
    }

    protected async onAction(payload: ActionPayload): Promise<void> {
        // Nothing to do, but we're required to override the function
        return;
    }

    /**
     * Starts a new recording if one isn't already in progress. Note that this simply
     * creates a recording instance - whether or not recording is actively in progress
     * can be seen via the VoiceRecording class.
     * @returns {VoiceRecording} The recording.
     */
    public startRecording(): VoiceRecording {
        if (!this.matrixClient) throw new Error("Cannot start a recording without a MatrixClient");
        if (this.state.recording) throw new Error("A recording is already in progress");

        const recording = new VoiceRecording(this.matrixClient);

        // noinspection JSIgnoredPromiseFromCall - we can safely run this async
        this.updateState({ recording });

        return recording;
    }

    /**
     * Disposes of the current recording, no matter the state of it.
     * @returns {Promise<void>} Resolves when complete.
     */
    public disposeRecording(): Promise<void> {
        if (this.state.recording) {
            this.state.recording.destroy(); // stops internally
        }
        return this.updateState({ recording: null });
    }
}

window.mxVoiceRecordingStore = VoiceRecordingStore.instance;
