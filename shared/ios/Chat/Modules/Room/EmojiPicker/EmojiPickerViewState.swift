// File created from ScreenTemplate
// $ createScreen.sh toto EmojiPicker

import Foundation

/// EmojiPickerViewController view state
enum EmojiPickerViewState {
    case loading
    case loaded(emojiCategories: [EmojiPickerCategoryViewData])
    case error(Error)
}
