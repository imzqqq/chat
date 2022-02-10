import Foundation

/// KeyBackupSetupPassphraseViewController view state
enum KeyBackupSetupPassphraseViewState {
    case loading
    case loaded
    case error(Error)
}
