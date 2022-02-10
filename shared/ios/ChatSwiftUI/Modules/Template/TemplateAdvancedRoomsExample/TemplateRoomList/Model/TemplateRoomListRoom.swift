import Foundation

struct TemplateRoomListRoom {
    let id: String
    let avatar: AvatarInput
    let displayName: String?
}

extension TemplateRoomListRoom: Identifiable, Equatable {}
