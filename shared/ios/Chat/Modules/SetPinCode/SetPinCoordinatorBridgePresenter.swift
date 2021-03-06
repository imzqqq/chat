// File created from FlowTemplate
// $ createRootCoordinator.sh SetPinCode SetPin EnterPinCode

import Foundation

@objc enum SetPinCoordinatorViewMode: Int {
    case setPin
    case setPinAfterLogin
    case setPinAfterRegister
    case notAllowedPin
    case unlock
    case confirmPinToDeactivate
    case setupBiometricsAfterLogin
    case setupBiometricsFromSettings
    case confirmBiometricsToDeactivate
    case inactive
    case changePin
}

@objc protocol SetPinCoordinatorBridgePresenterDelegate {
    func setPinCoordinatorBridgePresenterDelegateDidComplete(_ coordinatorBridgePresenter: SetPinCoordinatorBridgePresenter)
    @objc optional
    func setPinCoordinatorBridgePresenterDelegateDidCancel(_ coordinatorBridgePresenter: SetPinCoordinatorBridgePresenter)
    @objc optional
    func setPinCoordinatorBridgePresenterDelegateDidCompleteWithReset(_ coordinatorBridgePresenter: SetPinCoordinatorBridgePresenter, dueToTooManyErrors: Bool)
}

/// SetPinCoordinatorBridgePresenter enables to start SetPinCoordinator from a view controller.
/// This bridge is used while waiting for global usage of coordinator pattern.
@objcMembers
final class SetPinCoordinatorBridgePresenter: NSObject {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let session: MXSession?
    private var coordinator: SetPinCoordinator?
    var viewMode: SetPinCoordinatorViewMode {
        didSet {
            if viewMode != oldValue {
                coordinator?.viewMode = viewMode
            }
        }
    }
    
    // MARK: Public
    
    weak var delegate: SetPinCoordinatorBridgePresenterDelegate?
    
    // MARK: - Setup
    
    init(session: MXSession?, viewMode: SetPinCoordinatorViewMode) {
        self.session = session
        self.viewMode = viewMode
        super.init()
    }
    
    // MARK: - Public
    
    // NOTE: Default value feature is not compatible with Objective-C.
    // func present(from viewController: UIViewController, animated: Bool) {
    //     self.present(from: viewController, animated: animated)
    // }
    
    func present(from viewController: UIViewController, animated: Bool) {
        let setPinCoordinator = SetPinCoordinator(session: self.session, viewMode: self.viewMode, pinCodePreferences: .shared)
        setPinCoordinator.delegate = self
        viewController.present(setPinCoordinator.toPresentable(), animated: animated, completion: nil)
        setPinCoordinator.start()
        
        self.coordinator = setPinCoordinator
    }
    
    func present(in window: UIWindow) {
        let setPinCoordinator = SetPinCoordinator(session: self.session, viewMode: self.viewMode, pinCodePreferences: .shared)
        setPinCoordinator.delegate = self
        guard let view = setPinCoordinator.toPresentable().view else { return }
        window.addSubview(view)
        view.leadingAnchor.constraint(equalTo: window.leadingAnchor, constant: 0).isActive = true
        view.trailingAnchor.constraint(equalTo: window.trailingAnchor, constant: 0).isActive = true
        view.topAnchor.constraint(equalTo: window.topAnchor, constant: 0).isActive = true
        view.bottomAnchor.constraint(equalTo: window.bottomAnchor, constant: 0).isActive = true
        
        setPinCoordinator.start()
        
        self.coordinator = setPinCoordinator
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
    
    func dismiss() {
        guard let coordinator = self.coordinator else {
            return
        }
        coordinator.toPresentable().view.removeFromSuperview()
    }
}

// MARK: - SetPinCoordinatorDelegate
extension SetPinCoordinatorBridgePresenter: SetPinCoordinatorDelegate {
    
    func setPinCoordinatorDidComplete(_ coordinator: SetPinCoordinatorType) {
        self.delegate?.setPinCoordinatorBridgePresenterDelegateDidComplete(self)
    }
    
    func setPinCoordinatorDidCompleteWithReset(_ coordinator: SetPinCoordinatorType, dueToTooManyErrors: Bool) {
        self.delegate?.setPinCoordinatorBridgePresenterDelegateDidCompleteWithReset?(self, dueToTooManyErrors: dueToTooManyErrors)
    }
    
    func setPinCoordinatorDidCancel(_ coordinator: SetPinCoordinatorType) {
        self.delegate?.setPinCoordinatorBridgePresenterDelegateDidCancel?(self)
    }
}
