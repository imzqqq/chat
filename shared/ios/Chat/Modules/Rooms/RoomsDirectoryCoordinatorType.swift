// File created from FlowTemplate
// $ createRootCoordinator.sh Rooms2 RoomsDirectory ShowDirectory

import Foundation

protocol RoomsDirectoryCoordinatorDelegate: AnyObject {
    func roomsDirectoryCoordinator(_ coordinator: RoomsDirectoryCoordinatorType, didSelectRoom room: MXPublicRoom)
    func roomsDirectoryCoordinatorDidTapCreateNewRoom(_ coordinator: RoomsDirectoryCoordinatorType)
    func roomsDirectoryCoordinatorDidComplete(_ coordinator: RoomsDirectoryCoordinatorType)
    func roomsDirectoryCoordinator(_ coordinator: RoomsDirectoryCoordinatorType, didSelectRoomWithIdOrAlias roomIdOrAlias: String)
}

/// `RoomsDirectoryCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol RoomsDirectoryCoordinatorType: Coordinator, Presentable {
    var delegate: RoomsDirectoryCoordinatorDelegate? { get }
}
