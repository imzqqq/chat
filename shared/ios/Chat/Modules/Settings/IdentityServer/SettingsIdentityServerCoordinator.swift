// File created from FlowTemplate
// $ createRootCoordinator.sh Test SettingsIdentityServer

import UIKit

@objcMembers
final class SettingsIdentityServerCoordinator: SettingsIdentityServerCoordinatorType {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let session: MXSession
    private let settingsIdentityServerViewController: SettingsIdentityServerViewController
    
    // MARK: Public
    
    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    
    // MARK: - Setup
    
    init(session: MXSession) {
        self.session = session
        
        let settingsIdentityServerViewModel = SettingsIdentityServerViewModel(session: self.session)
        let settingsIdentityServerViewController = SettingsIdentityServerViewController.instantiate(with: settingsIdentityServerViewModel)
        self.settingsIdentityServerViewController = settingsIdentityServerViewController
    }
    
    // MARK: - Public methods
    
    func start() {
    }
    
    func toPresentable() -> UIViewController {
        return self.settingsIdentityServerViewController
    }
}
