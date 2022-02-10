import Foundation

protocol SpaceDetailViewModelViewDelegate: AnyObject {
    func spaceDetailViewModel(_ viewModel: SpaceDetailViewModelType, didUpdateViewState viewSate: SpaceDetailViewState)
}

protocol SpaceDetailModelViewModelCoordinatorDelegate: AnyObject {
    func spaceDetailViewModelDidCancel(_ viewModel: SpaceDetailViewModelType)
    func spaceDetailViewModelDidDismiss(_ viewModel: SpaceDetailViewModelType)
    func spaceDetailViewModelDidOpen(_ viewModel: SpaceDetailViewModelType)
    func spaceDetailViewModelDidJoin(_ viewModel: SpaceDetailViewModelType)
}

/// Protocol describing the view model used by `SpaceDetailViewController`
protocol SpaceDetailViewModelType {

    var viewDelegate: SpaceDetailViewModelViewDelegate? { get set }
    var coordinatorDelegate: SpaceDetailModelViewModelCoordinatorDelegate? { get set }

    func process(viewAction: SpaceDetailViewAction)
}
