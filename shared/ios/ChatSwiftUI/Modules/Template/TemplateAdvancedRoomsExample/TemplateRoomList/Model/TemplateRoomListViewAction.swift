import Foundation

/// Actions send from the `View` to the `ViewModel`.
enum TemplateRoomListViewAction {
    case done
    case didSelectRoom(String)
}
