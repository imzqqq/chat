// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceRoomList/ExploreRoom ShowSpaceExploreRoom

import Foundation
import UIKit

final class SpaceExploreRoomCoordinator: SpaceExploreRoomCoordinatorType {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private var spaceExploreRoomViewModel: SpaceExploreRoomViewModelType
    private let spaceExploreRoomViewController: SpaceExploreRoomViewController
    
    // MARK: Public

    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: SpaceExploreRoomCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(parameters: SpaceExploreRoomCoordinatorParameters) {
        let spaceExploreRoomViewModel = SpaceExploreRoomViewModel(parameters: parameters)
        let spaceExploreRoomViewController = SpaceExploreRoomViewController.instantiate(with: spaceExploreRoomViewModel)
        self.spaceExploreRoomViewModel = spaceExploreRoomViewModel
        self.spaceExploreRoomViewController = spaceExploreRoomViewController
    }
    
    // MARK: - Public methods
    
    func start() {            
        self.spaceExploreRoomViewModel.coordinatorDelegate = self
    }
    
    func toPresentable() -> UIViewController {
        return self.spaceExploreRoomViewController
    }
}

// MARK: - SpaceExploreRoomViewModelCoordinatorDelegate
extension SpaceExploreRoomCoordinator: SpaceExploreRoomViewModelCoordinatorDelegate {
    func spaceExploreRoomViewModel(_ viewModel: SpaceExploreRoomViewModelType, didSelect item: SpaceExploreRoomListItemViewData, from sourceView: UIView?) {
        self.delegate?.spaceExploreRoomCoordinator(self, didSelect: item, from: sourceView)
    }
    
    func spaceExploreRoomViewModelDidCancel(_ viewModel: SpaceExploreRoomViewModelType) {
        self.delegate?.spaceExploreRoomCoordinatorDidCancel(self)
    }
}
