// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceMembers/MemberList ShowSpaceMemberList

import Foundation

protocol SpaceMemberListCoordinatorDelegate: AnyObject {
    func spaceMemberListCoordinator(_ coordinator: SpaceMemberListCoordinatorType, didSelect member: MXRoomMember, from sourceView: UIView?)
    func spaceMemberListCoordinatorDidCancel(_ coordinator: SpaceMemberListCoordinatorType)
}

/// `SpaceMemberListCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol SpaceMemberListCoordinatorType: Coordinator, Presentable {
    var delegate: SpaceMemberListCoordinatorDelegate? { get }
}
