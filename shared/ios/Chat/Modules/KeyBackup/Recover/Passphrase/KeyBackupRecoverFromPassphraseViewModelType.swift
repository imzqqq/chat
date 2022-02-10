import Foundation

protocol KeyBackupRecoverFromPassphraseViewModelViewDelegate: AnyObject {
    func keyBackupRecoverFromPassphraseViewModel(_ viewModel: KeyBackupRecoverFromPassphraseViewModelType, didUpdateViewState viewSate: KeyBackupRecoverFromPassphraseViewState)
}

protocol KeyBackupRecoverFromPassphraseViewModelCoordinatorDelegate: AnyObject {
    func keyBackupRecoverFromPassphraseViewModelDidRecover(_ viewModel: KeyBackupRecoverFromPassphraseViewModelType)
    func keyBackupRecoverFromPassphraseViewModelDidCancel(_ viewModel: KeyBackupRecoverFromPassphraseViewModelType)
    func keyBackupRecoverFromPassphraseViewModelDoNotKnowPassphrase(_ viewModel: KeyBackupRecoverFromPassphraseViewModelType)
}

/// Protocol describing the view model used by `KeyBackupRecoverFromPassphraseViewController`
protocol KeyBackupRecoverFromPassphraseViewModelType {
    
    var passphrase: String? { get set }
    var isFormValid: Bool { get }
    
    var viewDelegate: KeyBackupRecoverFromPassphraseViewModelViewDelegate? { get set }
    var coordinatorDelegate: KeyBackupRecoverFromPassphraseViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: KeyBackupRecoverFromPassphraseViewAction)
}
