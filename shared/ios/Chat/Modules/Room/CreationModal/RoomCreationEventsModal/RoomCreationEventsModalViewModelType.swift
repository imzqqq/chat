// File created from ScreenTemplate
// $ createScreen.sh Modal2/RoomCreation RoomCreationEventsModal

import Foundation

protocol RoomCreationEventsModalViewModelViewDelegate: AnyObject {
    func roomCreationEventsModalViewModel(_ viewModel: RoomCreationEventsModalViewModelType, didUpdateViewState viewSate: RoomCreationEventsModalViewState)
}

protocol RoomCreationEventsModalViewModelCoordinatorDelegate: AnyObject {
    func roomCreationEventsModalViewModelDidTapClose(_ viewModel: RoomCreationEventsModalViewModelType)
}

/// Protocol describing the view model used by `RoomCreationEventsModalViewController`
protocol RoomCreationEventsModalViewModelType {
        
    var viewDelegate: RoomCreationEventsModalViewModelViewDelegate? { get set }
    var coordinatorDelegate: RoomCreationEventsModalViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: RoomCreationEventsModalViewAction)
    var numberOfRows: Int { get }
    func rowViewModel(at indexPath: IndexPath) -> RoomCreationEventRowViewModel?
    var roomName: String? { get }
    var roomInfo: String? { get }
    func setAvatar(in avatarImageView: MXKImageView)
    func setEncryptionIcon(in imageView: UIImageView)
}
