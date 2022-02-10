// File created from ScreenTemplate
// $ createScreen.sh Room2/RoomInfo RoomInfoList

import Foundation

/// RoomInfoListViewController view state
enum RoomInfoListViewState {
    case loading
    case loaded(viewData: RoomInfoListViewData)
    case error(Error)
}
