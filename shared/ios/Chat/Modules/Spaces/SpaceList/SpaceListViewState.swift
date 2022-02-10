// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceList SpaceList

import Foundation

/// SpaceListViewController view state
enum SpaceListViewState {
    case loading
    case loaded(_ sections: [SpaceListSection])
    case selectionChanged(_ indexPath: IndexPath)
    case error(Error)
}
