// File created from ScreenTemplate
// $ createScreen.sh .KeyBackup/Recover/PrivateKey KeyBackupRecoverFromPrivateKey

import Foundation

/// KeyBackupRecoverFromPrivateKeyViewController view state
enum KeyBackupRecoverFromPrivateKeyViewState {
    case loading
    case loaded
    case error(Error)
}
