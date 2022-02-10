import Foundation

@available(iOS 14.0, *)
@objc protocol NotificationSettingsCoordinatorBridgePresenterDelegate {
    func notificationSettingsCoordinatorBridgePresenterDelegateDidComplete(_ coordinatorBridgePresenter: NotificationSettingsCoordinatorBridgePresenter)
}

/// NotificationSettingsCoordinatorBridgePresenter enables to start NotificationSettingsCoordinator from a view controller.
/// This bridge is used while waiting for global usage of coordinator pattern.
/// It breaks the Coordinator abstraction and it has been introduced for Objective-C compatibility (mainly for integration in legacy view controllers).
/// Each bridge should be removed once the underlying Coordinator has been integrated by another Coordinator.
@available(iOS 14.0, *)
@objcMembers
final class NotificationSettingsCoordinatorBridgePresenter: NSObject {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let session: MXSession
    private var coordinator: NotificationSettingsCoordinator?
    private var router: NavigationRouterType?
    
    // MARK: Public
    
    weak var delegate: NotificationSettingsCoordinatorBridgePresenterDelegate?
    
    // MARK: - Setup
    
    init(session: MXSession) {
        self.session = session
        super.init()
    }
    
    // MARK: - Public
    
    func push(from navigationController: UINavigationController, animated: Bool, screen: NotificationSettingsScreen, popCompletion: (() -> Void)?) {
        
        let router = NavigationRouterStore.shared.navigationRouter(for: navigationController)
        
        let notificationSettingsCoordinator = NotificationSettingsCoordinator(session: session, screen: screen)
        
        router.push(notificationSettingsCoordinator, animated: animated) { [weak self] in
            self?.coordinator = nil
            self?.router = nil
            popCompletion?()
        }
        
        notificationSettingsCoordinator.start()
        
        self.coordinator = notificationSettingsCoordinator
        self.router = router
    }
    
    func dismiss(animated: Bool, completion: (() -> Void)?) {
        guard let coordinator = self.coordinator else {
            return
        }
        coordinator.toPresentable().dismiss(animated: animated) {
            self.coordinator = nil

            if let completion = completion {
                completion()
            }
        }
    }
}

// MARK: - NotificationSettingsCoordinatorDelegate
@available(iOS 14.0, *)
extension NotificationSettingsCoordinatorBridgePresenter: NotificationSettingsCoordinatorDelegate {
    func notificationSettingsCoordinatorDidComplete(_ coordinator: NotificationSettingsCoordinatorType) {
        self.delegate?.notificationSettingsCoordinatorBridgePresenterDelegateDidComplete(self)
    }
}

// MARK: - UIAdaptivePresentationControllerDelegate

@available(iOS 14.0, *)
extension NotificationSettingsCoordinatorBridgePresenter: UIAdaptivePresentationControllerDelegate {
    
    func notificationSettingsCoordinatorDidComplete(_ presentationController: UIPresentationController) {
        self.delegate?.notificationSettingsCoordinatorBridgePresenterDelegateDidComplete(self)
    }
    
}
