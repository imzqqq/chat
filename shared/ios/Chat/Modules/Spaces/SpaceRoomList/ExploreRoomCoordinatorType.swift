// File created from FlowTemplate
// $ createRootCoordinator.sh Spaces/SpaceRoomList ExploreRoom ShowSpaceExploreRoom

import Foundation

protocol ExploreRoomCoordinatorDelegate: AnyObject {
    func exploreRoomCoordinatorDidComplete(_ coordinator: ExploreRoomCoordinatorType)
}

/// `ExploreRoomCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol ExploreRoomCoordinatorType: Coordinator, Presentable {
    var delegate: ExploreRoomCoordinatorDelegate? { get }
}
