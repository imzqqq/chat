import Foundation

enum DiscussionType {
    case directMessage
    case multipleDirectMessage
    case room(topic: String?)
}

struct RoomCreationIntroViewData {
    let dicussionType: DiscussionType
    let roomDisplayName: String
    let avatarViewData: RoomAvatarViewData
}
