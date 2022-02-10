import Foundation

@objc
enum RoomInfoSection: Int {
    case none
    case addParticipants
    case changeAvatar
    case changeTopic
}

@objcMembers
class RoomInfoCoordinatorParameters: NSObject {
    
    let session: MXSession
    let room: MXRoom
    let initialSection: RoomInfoSection
    
    init(session: MXSession, room: MXRoom, initialSection: RoomInfoSection) {
        self.session = session
        self.room = room
        self.initialSection = initialSection
        super.init()
    }
    
    convenience init(session: MXSession, room: MXRoom) {
        self.init(session: session, room: room, initialSection: .none)
    }
}
