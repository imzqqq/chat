import Foundation

/// Actions sent by the`ViewModel` to the `Coordinator`.
enum TemplateRoomListViewModelAction {
    case didSelectRoom(String)
    case done
}
