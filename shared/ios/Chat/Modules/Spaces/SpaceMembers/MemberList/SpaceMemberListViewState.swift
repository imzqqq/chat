// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceMembers/MemberList ShowSpaceMemberList

import Foundation

/// SpaceMemberListViewController view state
enum SpaceMemberListViewState {
    case loading
    case loaded(_ space: MXSpace)
    case error(Error)
}
