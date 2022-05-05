/**
 * Fires when the middle panel has been resized (throttled).
 * @event module:utils~ResizeNotifier#"middlePanelResized"
 */
/**
 * Fires when the middle panel has been resized by a pixel.
 * @event module:utils~ResizeNotifier#"middlePanelResizedNoisy"
 */

import { EventEmitter } from "events";
import { throttle } from "lodash";

export default class ResizeNotifier extends EventEmitter {
    private _isResizing = false;

    // with default options, will call fn once at first call, and then every x ms
    // if there was another call in that timespan
    private throttledMiddlePanel = throttle(() => this.emit("middlePanelResized"), 200);

    public get isResizing() {
        return this._isResizing;
    }

    public startResizing() {
        this._isResizing = true;
        this.emit("isResizing", true);
    }

    public stopResizing() {
        this._isResizing = false;
        this.emit("isResizing", false);
    }

    private noisyMiddlePanel() {
        this.emit("middlePanelResizedNoisy");
    }

    private updateMiddlePanel() {
        this.throttledMiddlePanel();
        this.noisyMiddlePanel();
    }

    // can be called in quick succession
    public notifyLeftHandleResized() {
        // don't emit event for own region
        this.updateMiddlePanel();
    }

    // can be called in quick succession
    public notifyRightHandleResized() {
        this.updateMiddlePanel();
    }

    public notifyTimelineHeightChanged() {
        this.updateMiddlePanel();
    }

    // can be called in quick succession
    public notifyWindowResized() {
        this.updateMiddlePanel();
    }
}

