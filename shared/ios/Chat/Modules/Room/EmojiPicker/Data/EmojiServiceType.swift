import Foundation

protocol EmojiServiceType {
    func getEmojiCategories(completion: @escaping (MXResponse<[EmojiCategory]>) -> Void)
}
