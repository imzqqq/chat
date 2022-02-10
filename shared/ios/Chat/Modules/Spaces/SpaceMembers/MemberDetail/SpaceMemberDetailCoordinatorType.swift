// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceMembers/MemberDetail ShowSpaceMemberDetail

import Foundation

protocol SpaceMemberDetailCoordinatorDelegate: AnyObject {
    func spaceMemberDetailCoordinator(_ coordinator: SpaceMemberDetailCoordinatorType, showRoomWithId roomId: String)
    func spaceMemberDetailCoordinatorDidCancel(_ coordinator: SpaceMemberDetailCoordinatorType)
}

/// `SpaceMemberDetailCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol SpaceMemberDetailCoordinatorType: Coordinator, Presentable {
    var delegate: SpaceMemberDetailCoordinatorDelegate? { get }
}
