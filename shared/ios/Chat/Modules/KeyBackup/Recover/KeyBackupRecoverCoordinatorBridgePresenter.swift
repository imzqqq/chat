import Foundation

@objc protocol KeyBackupRecoverCoordinatorBridgePresenterDelegate {
    func keyBackupRecoverCoordinatorBridgePresenterDidCancel(_ keyBackupRecoverCoordinatorBridgePresenter: KeyBackupRecoverCoordinatorBridgePresenter)
    func keyBackupRecoverCoordinatorBridgePresenterDidRecover(_ keyBackupRecoverCoordinatorBridgePresenter: KeyBackupRecoverCoordinatorBridgePresenter)
}

/// KeyBackupRecoverCoordinatorBridgePresenter enables to start KeyBackupRecoverCoordinator from a view controller.
/// This bridge is used while waiting for global usage of coordinator pattern.
@objcMembers
final class KeyBackupRecoverCoordinatorBridgePresenter: NSObject {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let session: MXSession
    private let keyBackupVersion: MXKeyBackupVersion
    private var coordinator: KeyBackupRecoverCoordinator?
    
    // MARK: Public
    
    weak var delegate: KeyBackupRecoverCoordinatorBridgePresenterDelegate?
    
    // MARK: - Setup
    
    init(session: MXSession, keyBackupVersion: MXKeyBackupVersion) {
        self.session = session
        self.keyBackupVersion = keyBackupVersion
        super.init()
    }
    
    // MARK: - Public
    
    func present(from viewController: UIViewController, animated: Bool) {
        let keyBackupSetupCoordinator = KeyBackupRecoverCoordinator(session: self.session, keyBackupVersion: keyBackupVersion)
        keyBackupSetupCoordinator.delegate = self
        viewController.present(keyBackupSetupCoordinator.toPresentable(), animated: animated, completion: nil)
        keyBackupSetupCoordinator.start()
        
        self.coordinator = keyBackupSetupCoordinator
    }
    
    func push(from navigationController: UINavigationController, animated: Bool) {
        
        MXLog.debug("[KeyBackupRecoverCoordinatorBridgePresenter] Push complete security from \(navigationController)")
        
        let navigationRouter = NavigationRouterStore.shared.navigationRouter(for: navigationController)
        
        let keyBackupSetupCoordinator = KeyBackupRecoverCoordinator(session: self.session, keyBackupVersion: keyBackupVersion, navigationRouter: navigationRouter)
        keyBackupSetupCoordinator.delegate = self
        keyBackupSetupCoordinator.start() // Will trigger view controller push
        
        self.coordinator = keyBackupSetupCoordinator
    }
    
    func dismiss(animated: Bool) {
        guard let coordinator = self.coordinator else {
            return
        }
        coordinator.toPresentable().dismiss(animated: animated) {
            self.coordinator = nil
        }
    }
}

// MARK: - KeyBackupRecoverCoordinatorDelegate
extension KeyBackupRecoverCoordinatorBridgePresenter: KeyBackupRecoverCoordinatorDelegate {
    func keyBackupRecoverCoordinatorDidRecover(_ keyBackupRecoverCoordinator: KeyBackupRecoverCoordinatorType) {
        self.delegate?.keyBackupRecoverCoordinatorBridgePresenterDidRecover(self)
    }
    
    func keyBackupRecoverCoordinatorDidCancel(_ keyBackupRecoverCoordinator: KeyBackupRecoverCoordinatorType) {
        self.delegate?.keyBackupRecoverCoordinatorBridgePresenterDidCancel(self)
    }
}
