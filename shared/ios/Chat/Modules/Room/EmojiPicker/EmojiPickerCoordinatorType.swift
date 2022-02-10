// File created from ScreenTemplate
// $ createScreen.sh toto EmojiPicker

import Foundation

protocol EmojiPickerCoordinatorDelegate: AnyObject {
    func emojiPickerCoordinator(_ coordinator: EmojiPickerCoordinatorType, didAddEmoji emoji: String, forEventId eventId: String)
    func emojiPickerCoordinator(_ coordinator: EmojiPickerCoordinatorType, didRemoveEmoji emoji: String, forEventId eventId: String)
    func emojiPickerCoordinatorDidCancel(_ coordinator: EmojiPickerCoordinatorType)
}

/// `EmojiPickerCoordinatorType` is a protocol describing a Coordinator that handle emoji picker navigation flow.
protocol EmojiPickerCoordinatorType: Coordinator, Presentable {
    var delegate: EmojiPickerCoordinatorDelegate? { get }
}
