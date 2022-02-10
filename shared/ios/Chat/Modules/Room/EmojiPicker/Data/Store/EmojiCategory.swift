import Foundation

struct EmojiCategory {
    
    /// Emoji category identifier (e.g. "people")
    let identifier: String
    
    /// Emoji list associated to category
    let emojis: [EmojiItem]
    
    /// Emoji category localized name
    var name: String {
        let categoryNameLocalizationKey = "emoji_picker_\(self.identifier)_category"
        return VectorL10n.tr("Vector", categoryNameLocalizationKey)
    }
}
