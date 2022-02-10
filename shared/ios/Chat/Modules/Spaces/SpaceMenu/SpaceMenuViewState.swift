import Foundation

/// SpaceMenuViewController view state
enum SpaceMenuViewState {
    case loading
    case loaded
    case deselect
    case leaveOptions(_ displayName: String, _ isAdmin: Bool)
    case error(Error)
}
