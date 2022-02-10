// File created from ScreenTemplate
// $ createScreen.sh DeviceVerification/Start DeviceVerificationStart

import Foundation
import UIKit

final class DeviceVerificationStartCoordinator: DeviceVerificationStartCoordinatorType {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let session: MXSession
    private var deviceVerificationStartViewModel: DeviceVerificationStartViewModelType
    private let deviceVerificationStartViewController: DeviceVerificationStartViewController
    
    // MARK: Public

    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: DeviceVerificationStartCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(session: MXSession, otherUser: MXUser, otherDevice: MXDeviceInfo) {
        self.session = session
        
        let deviceVerificationStartViewModel = DeviceVerificationStartViewModel(session: self.session, otherUser: otherUser, otherDevice: otherDevice)
        let deviceVerificationStartViewController = DeviceVerificationStartViewController.instantiate(with: deviceVerificationStartViewModel)
        self.deviceVerificationStartViewModel = deviceVerificationStartViewModel
        self.deviceVerificationStartViewController = deviceVerificationStartViewController
    }
    
    // MARK: - Public methods
    
    func start() {            
        self.deviceVerificationStartViewModel.coordinatorDelegate = self
    }
    
    func toPresentable() -> UIViewController {
        return self.deviceVerificationStartViewController
    }
}

// MARK: - DeviceVerificationStartViewModelCoordinatorDelegate
extension DeviceVerificationStartCoordinator: DeviceVerificationStartViewModelCoordinatorDelegate {
    func deviceVerificationStartViewModelDidUseLegacyVerification(_ viewModel: DeviceVerificationStartViewModelType) {
        self.delegate?.deviceVerificationStartCoordinatorDidCancel(self) 
    }

    func deviceVerificationStartViewModel(_ viewModel: DeviceVerificationStartViewModelType, didCompleteWithOutgoingTransaction transaction: MXSASTransaction) {
        self.delegate?.deviceVerificationStartCoordinator(self, didCompleteWithOutgoingTransaction: transaction)
    }

    func deviceVerificationStartViewModel(_ viewModel: DeviceVerificationStartViewModelType, didTransactionCancelled transaction: MXSASTransaction) {
        self.delegate?.deviceVerificationStartCoordinator(self, didTransactionCancelled: transaction)
    }
    
    func deviceVerificationStartViewModelDidCancel(_ viewModel: DeviceVerificationStartViewModelType) {
        self.delegate?.deviceVerificationStartCoordinatorDidCancel(self)
    }
}
