// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceRoomList/ExploreRoom ShowSpaceExploreRoom

import Foundation

protocol SpaceExploreRoomViewModelViewDelegate: AnyObject {
    func spaceExploreRoomViewModel(_ viewModel: SpaceExploreRoomViewModelType, didUpdateViewState viewSate: SpaceExploreRoomViewState)
}

protocol SpaceExploreRoomViewModelCoordinatorDelegate: AnyObject {
    func spaceExploreRoomViewModel(_ viewModel: SpaceExploreRoomViewModelType, didSelect item: SpaceExploreRoomListItemViewData, from sourceView: UIView?)
    func spaceExploreRoomViewModelDidCancel(_ viewModel: SpaceExploreRoomViewModelType)
}

/// Protocol describing the view model used by `SpaceExploreRoomViewController`
protocol SpaceExploreRoomViewModelType {        
        
    var viewDelegate: SpaceExploreRoomViewModelViewDelegate? { get set }
    var coordinatorDelegate: SpaceExploreRoomViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: SpaceExploreRoomViewAction)
}
