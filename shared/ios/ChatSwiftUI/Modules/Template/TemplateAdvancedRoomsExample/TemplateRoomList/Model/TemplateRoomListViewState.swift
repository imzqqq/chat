import Foundation

/// State managed by the `ViewModel` delivered to the `View`.
struct TemplateRoomListViewState: BindableState {
    var rooms: [TemplateRoomListRoom]
}
