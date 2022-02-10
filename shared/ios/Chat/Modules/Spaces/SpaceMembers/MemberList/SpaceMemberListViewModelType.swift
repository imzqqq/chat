// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceMembers/MemberList ShowSpaceMemberList

import Foundation

protocol SpaceMemberListViewModelViewDelegate: AnyObject {
    func spaceMemberListViewModel(_ viewModel: SpaceMemberListViewModelType, didUpdateViewState viewSate: SpaceMemberListViewState)
}

protocol SpaceMemberListViewModelCoordinatorDelegate: AnyObject {
    func spaceMemberListViewModel(_ viewModel: SpaceMemberListViewModelType, didSelect member: MXRoomMember, from sourceView: UIView?)
    func spaceMemberListViewModelDidCancel(_ viewModel: SpaceMemberListViewModelType)
}

/// Protocol describing the view model used by `SpaceMemberListViewController`
protocol SpaceMemberListViewModelType {        
        
    var viewDelegate: SpaceMemberListViewModelViewDelegate? { get set }
    var coordinatorDelegate: SpaceMemberListViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: SpaceMemberListViewAction)
}
