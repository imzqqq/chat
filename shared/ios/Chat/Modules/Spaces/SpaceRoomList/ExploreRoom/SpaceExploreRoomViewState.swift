// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceRoomList/ExploreRoom ShowSpaceExploreRoom

import Foundation

/// SpaceExploreRoomViewController view state
enum SpaceExploreRoomViewState {
    case loading
    case spaceNameFound(_ spaceName: String)
    case loaded(_ children: [SpaceExploreRoomListItemViewData], _ hasMore: Bool)
    case emptySpace
    case emptyFilterResult
    case error(Error)
}
