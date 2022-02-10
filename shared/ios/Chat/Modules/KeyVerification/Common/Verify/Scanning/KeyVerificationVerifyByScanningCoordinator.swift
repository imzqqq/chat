// File created from ScreenTemplate
// $ createScreen.sh Verify KeyVerificationVerifyByScanning

import Foundation
import UIKit

final class KeyVerificationVerifyByScanningCoordinator: KeyVerificationVerifyByScanningCoordinatorType {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let session: MXSession
    private let keyVerificationRequest: MXKeyVerificationRequest
    
    private var keyVerificationVerifyByScanningViewModel: KeyVerificationVerifyByScanningViewModelType
    private let keyVerificationVerifyByScanningViewController: KeyVerificationVerifyByScanningViewController
    
    // MARK: Public

    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: KeyVerificationVerifyByScanningCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(session: MXSession, verificationKind: KeyVerificationKind, keyVerificationRequest: MXKeyVerificationRequest) {
        self.session = session
        self.keyVerificationRequest = keyVerificationRequest
        
        let keyVerificationVerifyByScanningViewModel = KeyVerificationVerifyByScanningViewModel(session: self.session, verificationKind: verificationKind, keyVerificationRequest: keyVerificationRequest)
        let keyVerificationVerifyByScanningViewController = KeyVerificationVerifyByScanningViewController.instantiate(with: keyVerificationVerifyByScanningViewModel)
        self.keyVerificationVerifyByScanningViewModel = keyVerificationVerifyByScanningViewModel
        self.keyVerificationVerifyByScanningViewController = keyVerificationVerifyByScanningViewController
    }
    
    // MARK: - Public methods
    
    func start() {            
        self.keyVerificationVerifyByScanningViewModel.coordinatorDelegate = self
    }
    
    func toPresentable() -> UIViewController {
        return self.keyVerificationVerifyByScanningViewController
    }
}

// MARK: - KeyVerificationVerifyByScanningViewModelCoordinatorDelegate
extension KeyVerificationVerifyByScanningCoordinator: KeyVerificationVerifyByScanningViewModelCoordinatorDelegate {
    
    func keyVerificationVerifyByScanningViewModelDidCancel(_ viewModel: KeyVerificationVerifyByScanningViewModelType) {
        self.delegate?.keyVerificationVerifyByScanningCoordinatorDidCancel(self)
    }    
    
    func keyVerificationVerifyByScanningViewModel(_ viewModel: KeyVerificationVerifyByScanningViewModelType, didStartSASVerificationWithTransaction transaction: MXSASTransaction) {
        self.delegate?.keyVerificationVerifyByScanningCoordinator(self, didCompleteWithSASTransaction: transaction)
    }
    
    func keyVerificationVerifyByScanningViewModel(_ viewModel: KeyVerificationVerifyByScanningViewModelType, didScanOtherQRCodeData qrCodeData: MXQRCodeData, withTransaction transaction: MXQRCodeTransaction) {
        self.delegate?.keyVerificationVerifyByScanningCoordinator(self, didScanOtherQRCodeData: qrCodeData, withTransaction: transaction)
    }
    
    func keyVerificationVerifyByScanningViewModel(_ viewModel: KeyVerificationVerifyByScanningViewModelType, qrCodeDidScannedByOtherWithTransaction transaction: MXQRCodeTransaction) {
        self.delegate?.keyVerificationVerifyByScanningCoordinator(self, qrCodeDidScannedByOtherWithTransaction: transaction)
    }
}
