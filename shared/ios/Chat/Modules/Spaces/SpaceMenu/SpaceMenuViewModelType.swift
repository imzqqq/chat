import Foundation

protocol SpaceMenuViewModelViewDelegate: AnyObject {
    func spaceMenuViewModel(_ viewModel: SpaceMenuViewModelType, didUpdateViewState viewSate: SpaceMenuViewState)
}

protocol SpaceMenuModelViewModelCoordinatorDelegate: AnyObject {
    func spaceMenuViewModelDidDismiss(_ viewModel: SpaceMenuViewModelType)
    func spaceMenuViewModel(_ viewModel: SpaceMenuViewModelType, didSelectItemWith action: SpaceMenuListItemAction)
}

/// Protocol describing the view model used by `SpaceMenuViewController`
protocol SpaceMenuViewModelType {
    var menuItems: [SpaceMenuListItemViewData] { get }
    
    var viewDelegate: SpaceMenuViewModelViewDelegate? { get set }
    var coordinatorDelegate: SpaceMenuModelViewModelCoordinatorDelegate? { get set }

    func process(viewAction: SpaceMenuViewAction)
}
