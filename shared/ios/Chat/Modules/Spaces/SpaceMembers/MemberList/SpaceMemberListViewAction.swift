// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceMembers/MemberList ShowSpaceMemberList

import Foundation

/// SpaceMemberListViewController view actions exposed to view model
enum SpaceMemberListViewAction {
    case loadData
    case complete(_ selectedMember: MXRoomMember, _ sourceView: UIView?)
    case cancel
}
