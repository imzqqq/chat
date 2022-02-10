// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceMembers/MemberDetail ShowSpaceMemberDetail

import Foundation

/// SpaceMemberDetailViewController view actions exposed to view model
enum SpaceMemberDetailViewAction {
    case loadData
    case openRoom(_ roomId: String)
    case createRoom(_ memberId: String)
    case cancel
}
