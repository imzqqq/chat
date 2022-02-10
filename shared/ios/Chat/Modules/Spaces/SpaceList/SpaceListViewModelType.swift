// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceList SpaceList

import Foundation

protocol SpaceListViewModelViewDelegate: AnyObject {
    func spaceListViewModel(_ viewModel: SpaceListViewModelType, didUpdateViewState viewSate: SpaceListViewState)
}

protocol SpaceListViewModelCoordinatorDelegate: AnyObject {
    func spaceListViewModelDidSelectHomeSpace(_ viewModel: SpaceListViewModelType)
    func spaceListViewModel(_ viewModel: SpaceListViewModelType, didSelectSpaceWithId spaceId: String)
    func spaceListViewModel(_ viewModel: SpaceListViewModelType, didSelectInviteWithId spaceId: String, from sourceView: UIView?)
    func spaceListViewModel(_ viewModel: SpaceListViewModelType, didPressMoreForSpaceWithId spaceId: String, from sourceView: UIView)
}

/// Protocol describing the view model used by `SpaceListViewController`
protocol SpaceListViewModelType {        
        
    var viewDelegate: SpaceListViewModelViewDelegate? { get set }
    var coordinatorDelegate: SpaceListViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: SpaceListViewAction)
    func revertItemSelection()
    func select(spaceWithId spaceId: String)
}
