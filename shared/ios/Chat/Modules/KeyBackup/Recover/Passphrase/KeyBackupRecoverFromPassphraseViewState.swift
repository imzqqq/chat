import Foundation

/// KeyBackupRecoverFromPassphraseViewController view state
enum KeyBackupRecoverFromPassphraseViewState {
    case loading
    case loaded
    case error(Error)
}
