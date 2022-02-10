import Foundation

/// `SpaceMenuViewController` view actions exposed to view model
enum SpaceMenuViewAction {
    case selectRow(at: IndexPath)
    case leaveSpaceAndKeepRooms
    case leaveSpaceAndLeaveRooms
    case dismiss
}
