// File created from FlowTemplate
// $ createRootCoordinator.sh Room RoomCreationModal RoomCreationEventsModal

import Foundation

protocol RoomCreationModalCoordinatorDelegate: AnyObject {
    func roomCreationModalCoordinatorDidComplete(_ coordinator: RoomCreationModalCoordinatorType)
}

/// `RoomCreationModalCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol RoomCreationModalCoordinatorType: Coordinator, Presentable {
    var delegate: RoomCreationModalCoordinatorDelegate? { get }
}
