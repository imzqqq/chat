import BasePlatform from "./BasePlatform";

/*
 * Holds the current Platform object used by the code to do anything
 * specific to the platform we're running on (eg. web, electron)
 * Platforms are provided by the app layer.
 * This allows the app layer to set a Platform without necessarily
 * having to have a MatrixChat object
 */
export class PlatformPeg {
    platform: BasePlatform = null;

    /**
     * Returns the current Platform object for the application.
     * This should be an instance of a class extending BasePlatform.
     */
    get() {
        return this.platform;
    }

    /**
     * Sets the current platform handler object to use for the
     * application.
     * This should be an instance of a class extending BasePlatform.
     */
    set(plaf: BasePlatform) {
        this.platform = plaf;
    }
}

if (!window.mxPlatformPeg) {
    window.mxPlatformPeg = new PlatformPeg();
}
export default window.mxPlatformPeg;
