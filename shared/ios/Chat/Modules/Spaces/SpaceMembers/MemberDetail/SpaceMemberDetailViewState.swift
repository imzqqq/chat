// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceMembers/MemberDetail ShowSpaceMemberDetail

import Foundation

/// SpaceMemberDetailViewController view state
enum SpaceMemberDetailViewState {
    case loading
    case loaded(MXRoomMember, MXRoom?)
    case error(Error)
}
