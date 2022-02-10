import Foundation

/// Actions returned by the coordinator callback
enum TemplateRoomListCoordinatorAction {
    case didSelectRoom(String)
    case done
}
