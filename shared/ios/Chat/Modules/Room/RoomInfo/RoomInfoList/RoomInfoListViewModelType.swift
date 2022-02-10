// File created from ScreenTemplate
// $ createScreen.sh Room2/RoomInfo RoomInfoList

import Foundation

protocol RoomInfoListViewModelViewDelegate: AnyObject {
    func roomInfoListViewModel(_ viewModel: RoomInfoListViewModelType, didUpdateViewState viewSate: RoomInfoListViewState)
}

protocol RoomInfoListViewModelCoordinatorDelegate: AnyObject {
    func roomInfoListViewModelDidCancel(_ viewModel: RoomInfoListViewModelType)
    func roomInfoListViewModelDidLeaveRoom(_ viewModel: RoomInfoListViewModelType)
    func roomInfoListViewModel(_ viewModel: RoomInfoListViewModelType, wantsToNavigateTo target: RoomInfoListTarget)
}

/// Protocol describing the view model used by `RoomInfoListViewController`
protocol RoomInfoListViewModelType {        
        
    var viewDelegate: RoomInfoListViewModelViewDelegate? { get set }
    var coordinatorDelegate: RoomInfoListViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: RoomInfoListViewAction)    
}
