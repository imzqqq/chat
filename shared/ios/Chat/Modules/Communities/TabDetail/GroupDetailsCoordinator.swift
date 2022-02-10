// File created from ScreenTemplate
// $ createScreen.sh Communities GroupDetails

import Foundation
import UIKit

final class GroupDetailsCoordinator: GroupDetailsCoordinatorProtocol {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let parameters: GroupDetailsCoordinatorParameters
    private let groupDetailsViewController: GroupDetailsViewController
    
    // MARK: Public

    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: GroupDetailsCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(parameters: GroupDetailsCoordinatorParameters) {
        self.parameters = parameters
        let groupDetailsViewController: GroupDetailsViewController = GroupDetailsViewController.instantiate()
        self.groupDetailsViewController = groupDetailsViewController
    }
    
    deinit {
        groupDetailsViewController.destroy()
    }
    
    // MARK: - Public
    
    func start() {
        self.groupDetailsViewController.setGroup(self.parameters.group, withMatrixSession: self.parameters.session)
    }
    
    func toPresentable() -> UIViewController {
        return self.groupDetailsViewController
    }
}
