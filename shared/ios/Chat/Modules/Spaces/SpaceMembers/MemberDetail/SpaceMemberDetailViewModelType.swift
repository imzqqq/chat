// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceMembers/MemberDetail ShowSpaceMemberDetail

import Foundation

protocol SpaceMemberDetailViewModelViewDelegate: AnyObject {
    func spaceMemberDetailViewModel(_ viewModel: SpaceMemberDetailViewModelType, didUpdateViewState viewSate: SpaceMemberDetailViewState)
}

protocol SpaceMemberDetailViewModelCoordinatorDelegate: AnyObject {
    func spaceMemberDetailViewModel(_ viewModel: SpaceMemberDetailViewModelType, showRoomWithId roomId: String)
    func spaceMemberDetailViewModelDidCancel(_ viewModel: SpaceMemberDetailViewModelType)
}

/// Protocol describing the view model used by `SpaceMemberDetailViewController`
protocol SpaceMemberDetailViewModelType {        
        
    var viewDelegate: SpaceMemberDetailViewModelViewDelegate? { get set }
    var coordinatorDelegate: SpaceMemberDetailViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: SpaceMemberDetailViewAction)
}
