// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceList SpaceList

import Foundation

/// SpaceListViewController view actions exposed to view model
enum SpaceListViewAction {
    case loadData    
    case selectRow(at: IndexPath, from: UIView?)
    case moreAction(at: IndexPath, from: UIView)
}
