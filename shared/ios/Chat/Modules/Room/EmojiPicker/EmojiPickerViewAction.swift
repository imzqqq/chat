// File created from ScreenTemplate
// $ createScreen.sh toto EmojiPicker

import Foundation

/// EmojiPickerViewController view actions exposed to view model
enum EmojiPickerViewAction {
    case loadData
    case cancel
    case tap(emojiItemViewData: EmojiPickerItemViewData)
    case search(text: String?)
}
