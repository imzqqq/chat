// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceRoomList/ExploreRoom ShowSpaceExploreRoom

import Foundation

/// SpaceExploreRoomViewController view actions exposed to view model
enum SpaceExploreRoomViewAction {
    case loadData
    case complete(_ selectedItem: SpaceExploreRoomListItemViewData, _ sourceView: UIView?)
    case searchChanged(_ text: String?)
    case cancel
}
