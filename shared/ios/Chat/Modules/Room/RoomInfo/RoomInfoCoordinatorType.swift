// File created from FlowTemplate
// $ createRootCoordinator.sh Room2 RoomInfo RoomInfoList

import Foundation

protocol RoomInfoCoordinatorDelegate: AnyObject {
    func roomInfoCoordinatorDidComplete(_ coordinator: RoomInfoCoordinatorType)
    func roomInfoCoordinator(_ coordinator: RoomInfoCoordinatorType, didRequestMentionForMember member: MXRoomMember)
    func roomInfoCoordinatorDidLeaveRoom(_ coordinator: RoomInfoCoordinatorType)
}

/// `RoomInfoCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol RoomInfoCoordinatorType: Coordinator, Presentable {
    var delegate: RoomInfoCoordinatorDelegate? { get }
}
