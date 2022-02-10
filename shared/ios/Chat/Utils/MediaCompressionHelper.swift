import Foundation

/// A collection of helpful functions for media compression.
class MediaCompressionHelper: NSObject {
    /// The default compression mode taking into account the `roomInputToolbarCompressionMode` build setting
    /// and the `showMediaCompressionPrompt` Chat setting.
    @objc static var defaultCompressionMode: MXKRoomInputToolbarCompressionMode {
        // When the compression mode build setting hasn't been customised, use the media compression prompt setting to determine what to do.
        if BuildSettings.roomInputToolbarCompressionMode == MXKRoomInputToolbarCompressionModePrompt {
            return ChatSettings.shared.showMediaCompressionPrompt ? MXKRoomInputToolbarCompressionModePrompt : MXKRoomInputToolbarCompressionModeNone
        } else {
            // Otherwise use the compression mode defined in the build settings.
            return BuildSettings.roomInputToolbarCompressionMode
        }
    }
}
