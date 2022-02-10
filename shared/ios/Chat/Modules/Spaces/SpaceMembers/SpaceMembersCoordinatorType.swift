// File created from FlowTemplate
// $ createRootCoordinator.sh Spaces/SpaceMembers SpaceMemberList ShowSpaceMemberList

import Foundation

protocol SpaceMembersCoordinatorDelegate: AnyObject {
    func spaceMembersCoordinatorDidCancel(_ coordinator: SpaceMembersCoordinatorType)
}

/// `SpaceMembersCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol SpaceMembersCoordinatorType: Coordinator, Presentable {
    var delegate: SpaceMembersCoordinatorDelegate? { get }
}
