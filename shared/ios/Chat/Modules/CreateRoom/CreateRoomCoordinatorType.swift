// File created from FlowTemplate
// $ createRootCoordinator.sh CreateRoom CreateRoom EnterNewRoomDetails

import Foundation

protocol CreateRoomCoordinatorDelegate: AnyObject {
    func createRoomCoordinator(_ coordinator: CreateRoomCoordinatorType, didCreateNewRoom room: MXRoom)
    func createRoomCoordinatorDidCancel(_ coordinator: CreateRoomCoordinatorType)
}

/// `CreateRoomCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol CreateRoomCoordinatorType: Coordinator, Presentable {
    var delegate: CreateRoomCoordinatorDelegate? { get }
}
