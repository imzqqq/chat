import Foundation

/// Model for "im.vector.setting.allowed_widgets"
/// https://github.com/vector-im/riot-meta/blob/master/spec/settings.md#tracking-which-widgets-the-user-has-allowed-to-load
struct ChatSettingAllowedWidgets {
    let widgets: [String: Bool]

    // Widget type -> Server domain -> Bool
    let nativeWidgets: [String: [String: Bool]]
}

extension ChatSettingAllowedWidgets: Decodable {
    enum CodingKeys: String, CodingKey {
        case widgets
        case nativeWidgets = "native_widgets"
    }
}
