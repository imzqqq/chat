// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceRoomList/SpaceChildRoomDetail ShowSpaceChildRoomDetail

import Foundation

protocol SpaceChildRoomDetailCoordinatorDelegate: AnyObject {
    func spaceChildRoomDetailCoordinator(_ coordinator: SpaceChildRoomDetailCoordinatorType, didOpenRoomWith roomId: String)
    func spaceChildRoomDetailCoordinatorDidCancel(_ coordinator: SpaceChildRoomDetailCoordinatorType)
}

/// `SpaceChildRoomDetailCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol SpaceChildRoomDetailCoordinatorType: Coordinator, Presentable {
    var delegate: SpaceChildRoomDetailCoordinatorDelegate? { get }
}
