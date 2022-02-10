import Foundation

struct RoomCreationParameters {
    var name: String?
    var topic: String?
    var address: String?
    var avatarImage: UIImage? {
        return userSelectedAvatar ?? initialsAvatar
    }
    var isEncrypted: Bool = false
    var isPublic: Bool = false {
        didSet {
            if !isPublic {
                //  if set to private again, reset some fields
                showInDirectory = false
                address = nil
            }
        }
    }
    var showInDirectory: Bool = false
    
    var userSelectedAvatar: UIImage?
    var initialsAvatar: UIImage?
}
