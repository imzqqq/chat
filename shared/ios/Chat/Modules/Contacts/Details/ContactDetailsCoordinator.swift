// File created from ScreenTemplate
// $ createScreen.sh Contacts ContactDetails

import Foundation
import UIKit

final class ContactDetailsCoordinator: ContactDetailsCoordinatorProtocol {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let parameters: ContactDetailsCoordinatorParameters
    private let contactDetailsViewController: ContactDetailsViewController
    
    // MARK: Public

    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: ContactDetailsCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(parameters: ContactDetailsCoordinatorParameters) {
        self.parameters = parameters
        let contactDetailsViewController: ContactDetailsViewController = ContactDetailsViewController.instantiate()
        contactDetailsViewController.contact = self.parameters.contact
        contactDetailsViewController.enableVoipCall = self.parameters.enableVoipCall
        self.contactDetailsViewController = contactDetailsViewController
    }
    
    // MARK: - Public
    
    func start() {
    }
    
    func toPresentable() -> UIViewController {
        return self.contactDetailsViewController
    }
}
