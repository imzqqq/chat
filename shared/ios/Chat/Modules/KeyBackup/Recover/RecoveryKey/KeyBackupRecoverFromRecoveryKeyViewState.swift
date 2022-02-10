import Foundation

/// KeyBackupRecoverFromRecoveryKeyViewController view state
enum KeyBackupRecoverFromRecoveryKeyViewState {
    case loading
    case loaded
    case error(Error)
}
