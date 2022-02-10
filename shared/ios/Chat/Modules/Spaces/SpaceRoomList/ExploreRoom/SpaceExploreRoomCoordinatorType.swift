// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceRoomList/ExploreRoom ShowSpaceExploreRoom

import Foundation

protocol SpaceExploreRoomCoordinatorDelegate: AnyObject {
    func spaceExploreRoomCoordinator(_ coordinator: SpaceExploreRoomCoordinatorType, didSelect item: SpaceExploreRoomListItemViewData, from sourceView: UIView?)
    func spaceExploreRoomCoordinatorDidCancel(_ coordinator: SpaceExploreRoomCoordinatorType)
}

/// `SpaceExploreRoomCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol SpaceExploreRoomCoordinatorType: Coordinator, Presentable {
    var delegate: SpaceExploreRoomCoordinatorDelegate? { get }
}
