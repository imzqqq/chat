import Foundation

/// A user who is a member of the room.
struct TemplateRoomChatMember {
    let id: String
    let avatarUrl: String?
    let displayName: String?
}

extension TemplateRoomChatMember: Avatarable {
    var mxContentUri: String? {
        avatarUrl
    }
    
    var matrixItemId: String {
        id
    }
}

extension TemplateRoomChatMember: Identifiable, Equatable {}
