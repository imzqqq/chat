// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceList SpaceList

import Foundation

protocol SpaceListCoordinatorDelegate: AnyObject {
    func spaceListCoordinatorDidSelectHomeSpace(_ coordinator: SpaceListCoordinatorType)
    func spaceListCoordinator(_ coordinator: SpaceListCoordinatorType, didSelectSpaceWithId spaceId: String)
    func spaceListCoordinator(_ coordinator: SpaceListCoordinatorType, didSelectInviteWithId spaceId: String, from sourceView: UIView?)
    func spaceListCoordinator(_ coordinator: SpaceListCoordinatorType, didPressMoreForSpaceWithId spaceId: String, from sourceView: UIView)
}

/// `SpaceListCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol SpaceListCoordinatorType: Coordinator, Presentable {
    var delegate: SpaceListCoordinatorDelegate? { get }
    func revertItemSelection()
    func select(spaceWithId spaceId: String)
}
