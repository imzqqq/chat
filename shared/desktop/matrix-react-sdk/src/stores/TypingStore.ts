import { MatrixClientPeg } from "../MatrixClientPeg";
import SettingsStore from "../settings/SettingsStore";
import Timer from "../utils/Timer";

const TYPING_USER_TIMEOUT = 10000;
const TYPING_SERVER_TIMEOUT = 30000;

/**
 * Tracks typing state for users.
 */
export default class TypingStore {
    private typingStates: {
        [roomId: string]: {
            isTyping: boolean;
            userTimer: Timer;
            serverTimer: Timer;
        };
    };

    constructor() {
        this.reset();
    }

    static sharedInstance(): TypingStore {
        if (window.mxTypingStore === undefined) {
            window.mxTypingStore = new TypingStore();
        }
        return window.mxTypingStore;
    }

    /**
     * Clears all cached typing states. Intended to be called when the
     * MatrixClientPeg client changes.
     */
    reset() {
        this.typingStates = {
            // "roomId": {
            //     isTyping: bool,     // Whether the user is typing or not
            //     userTimer: Timer,   // Local timeout for "user has stopped typing"
            //     serverTimer: Timer, // Maximum timeout for the typing state
            // },
        };
    }

    /**
     * Changes the typing status for the MatrixClientPeg user.
     * @param {string} roomId The room ID to set the typing state in.
     * @param {boolean} isTyping Whether the user is typing or not.
     */
    setSelfTyping(roomId: string, isTyping: boolean): void {
        if (!SettingsStore.getValue('sendTypingNotifications')) return;
        if (SettingsStore.getValue('lowBandwidth')) return;

        let currentTyping = this.typingStates[roomId];
        if ((!isTyping && !currentTyping) || (currentTyping && currentTyping.isTyping === isTyping)) {
            // No change in state, so don't do anything. We'll let the timer run its course.
            return;
        }

        if (!currentTyping) {
            currentTyping = this.typingStates[roomId] = {
                isTyping: isTyping,
                serverTimer: new Timer(TYPING_SERVER_TIMEOUT),
                userTimer: new Timer(TYPING_USER_TIMEOUT),
            };
        }

        currentTyping.isTyping = isTyping;

        if (isTyping) {
            if (!currentTyping.serverTimer.isRunning()) {
                currentTyping.serverTimer.restart().finished().then(() => {
                    const currentTyping = this.typingStates[roomId];
                    if (currentTyping) currentTyping.isTyping = false;

                    // The server will (should) time us out on typing, so we don't
                    // need to advertise a stop of typing.
                });
            } else currentTyping.serverTimer.restart();

            if (!currentTyping.userTimer.isRunning()) {
                currentTyping.userTimer.restart().finished().then(() => {
                    this.setSelfTyping(roomId, false);
                });
            } else currentTyping.userTimer.restart();
        }

        MatrixClientPeg.get().sendTyping(roomId, isTyping, TYPING_SERVER_TIMEOUT);
    }
}
