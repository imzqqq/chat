// File created from ScreenTemplate
// $ createScreen.sh KeyVerification/Common/ScanConfirmation KeyVerificationScanConfirmation

import Foundation
import UIKit

enum KeyVerificationScanning {
    case scannedOtherQRCode(MXQRCodeData)
    case myQRCodeScanned
}

final class KeyVerificationScanConfirmationCoordinator: KeyVerificationScanConfirmationCoordinatorType {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let session: MXSession
    private var keyVerificationScanConfirmationViewModel: KeyVerificationScanConfirmationViewModelType
    private let keyVerificationScanConfirmationViewController: KeyVerificationScanConfirmationViewController
    
    // MARK: Public

    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: KeyVerificationScanConfirmationCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(session: MXSession, transaction: MXQRCodeTransaction, codeScanning: KeyVerificationScanning, verificationKind: KeyVerificationKind) {
        self.session = session
        
        let keyVerificationScanConfirmationViewModel = KeyVerificationScanConfirmationViewModel(session: self.session, transaction: transaction, codeScanning: codeScanning, verificationKind: verificationKind)
        let keyVerificationScanConfirmationViewController = KeyVerificationScanConfirmationViewController.instantiate(with: keyVerificationScanConfirmationViewModel)
        self.keyVerificationScanConfirmationViewModel = keyVerificationScanConfirmationViewModel
        self.keyVerificationScanConfirmationViewController = keyVerificationScanConfirmationViewController
    }
    
    // MARK: - Public methods
    
    func start() {            
        self.keyVerificationScanConfirmationViewModel.coordinatorDelegate = self
    }
    
    func toPresentable() -> UIViewController {
        return self.keyVerificationScanConfirmationViewController
    }
}

// MARK: - KeyVerificationScanConfirmationViewModelCoordinatorDelegate
extension KeyVerificationScanConfirmationCoordinator: KeyVerificationScanConfirmationViewModelCoordinatorDelegate {
    
    func keyVerificationScanConfirmationViewModelDidComplete(_ viewModel: KeyVerificationScanConfirmationViewModelType) {
        self.delegate?.keyVerificationScanConfirmationCoordinatorDidComplete(self)
    }
    
    func keyVerificationScanConfirmationViewModelDidCancel(_ viewModel: KeyVerificationScanConfirmationViewModelType) {
        self.delegate?.keyVerificationScanConfirmationCoordinatorDidCancel(self)
    }
}
