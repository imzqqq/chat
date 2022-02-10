import Foundation

protocol SpaceChildRoomDetailViewModelViewDelegate: AnyObject {
    func spaceChildRoomDetailViewModel(_ viewModel: SpaceChildRoomDetailViewModelType, didUpdateViewState viewSate: SpaceChildRoomDetailViewState)
}

protocol SpaceChildRoomDetailViewModelCoordinatorDelegate: AnyObject {
    func spaceChildRoomDetailViewModel(_ viewModel: SpaceChildRoomDetailViewModelType, didOpenRoomWith roomId: String)
    func spaceChildRoomDetailViewModelDidCancel(_ viewModel: SpaceChildRoomDetailViewModelType)
}

/// Protocol describing the view model used by `SpaceChildRoomDetailViewController`
protocol SpaceChildRoomDetailViewModelType {        
        
    var viewDelegate: SpaceChildRoomDetailViewModelViewDelegate? { get set }
    var coordinatorDelegate: SpaceChildRoomDetailViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: SpaceChildRoomDetailViewAction)
}
