// File created from ScreenTemplate
// $ createScreen.sh CreateRoom/EnterNewRoomDetails EnterNewRoomDetails

import Foundation

protocol EnterNewRoomDetailsViewModelViewDelegate: AnyObject {
    func enterNewRoomDetailsViewModel(_ viewModel: EnterNewRoomDetailsViewModelType, didUpdateViewState viewSate: EnterNewRoomDetailsViewState)
}

protocol EnterNewRoomDetailsViewModelCoordinatorDelegate: AnyObject {
    func enterNewRoomDetailsViewModel(_ viewModel: EnterNewRoomDetailsViewModelType, didCreateNewRoom room: MXRoom)
    func enterNewRoomDetailsViewModel(_ viewModel: EnterNewRoomDetailsViewModelType, didTapChooseAvatar sourceView: UIView)
    func enterNewRoomDetailsViewModelDidCancel(_ viewModel: EnterNewRoomDetailsViewModelType)
}

/// Protocol describing the view model used by `EnterNewRoomDetailsViewController`
protocol EnterNewRoomDetailsViewModelType {        
        
    var viewDelegate: EnterNewRoomDetailsViewModelViewDelegate? { get set }
    var coordinatorDelegate: EnterNewRoomDetailsViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: EnterNewRoomDetailsViewAction)
    
    var roomCreationParameters: RoomCreationParameters { get set }
    
    var viewState: EnterNewRoomDetailsViewState { get }
}
