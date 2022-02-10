// File created from FlowTemplate
// $ createRootCoordinator.sh Room RoomCreationModal RoomCreationEventsModal

import Foundation

@objc protocol RoomCreationModalCoordinatorBridgePresenterDelegate {
    func roomCreationModalCoordinatorBridgePresenterDelegateDidComplete(_ coordinatorBridgePresenter: RoomCreationModalCoordinatorBridgePresenter)
}

/// RoomCreationModalCoordinatorBridgePresenter enables to start RoomCreationModalCoordinator from a view controller.
/// This bridge is used while waiting for global usage of coordinator pattern.
@objcMembers
final class RoomCreationModalCoordinatorBridgePresenter: NSObject {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let session: MXSession
    private let roomState: MXRoomState
    private let bubbleData: MXKRoomBubbleCellDataStoring
    private var coordinator: RoomCreationEventsModalCoordinator?
    private lazy var slidingModalPresenter: SlidingModalPresenter = {
        return SlidingModalPresenter()
    }()
    
    // MARK: Public
    
    weak var delegate: RoomCreationModalCoordinatorBridgePresenterDelegate?
    
    // MARK: - Setup
    
    init(session: MXSession, bubbleData: MXKRoomBubbleCellDataStoring, roomState: MXRoomState) {
        self.session = session
        self.bubbleData = bubbleData
        self.roomState = roomState
        super.init()
    }
    
    // MARK: - Public
    
    func present(from viewController: UIViewController, animated: Bool) {
        slidingModalPresenter.dismiss(animated: false, completion: nil)
        let roomCreationEventsModalCoordinator = RoomCreationEventsModalCoordinator(session: self.session, bubbleData: bubbleData, roomState: roomState)
        roomCreationEventsModalCoordinator.delegate = self
        
        slidingModalPresenter.present(roomCreationEventsModalCoordinator.toSlidingModalPresentable(),
                                      from: viewController,
                                      animated: animated,
                                      options: .spanning,
                                      completion: nil)
        
        roomCreationEventsModalCoordinator.start()
        
        self.coordinator = roomCreationEventsModalCoordinator
    }
    
    func dismiss(animated: Bool, completion: (() -> Void)?) {
        slidingModalPresenter.dismiss(animated: animated, completion: completion)
    }
}

// MARK: - RoomCreationModalCoordinatorDelegate
extension RoomCreationModalCoordinatorBridgePresenter: RoomCreationEventsModalCoordinatorDelegate {
    
    func roomCreationEventsModalCoordinatorDidTapClose(_ coordinator: RoomCreationEventsModalCoordinatorType) {
        self.delegate?.roomCreationModalCoordinatorBridgePresenterDelegateDidComplete(self)
    }
    
}
