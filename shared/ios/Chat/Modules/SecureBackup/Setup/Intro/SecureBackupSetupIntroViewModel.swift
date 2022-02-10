import Foundation

final class SecureBackupSetupIntroViewModel: SecureBackupSetupIntroViewModelType {
    
    // MARK: - Properties

    // TODO: Make these properties private
    let keyBackup: MXKeyBackup?
    let checkKeyBackup: Bool
    
    // MARK: - Setup
    
    init(keyBackup: MXKeyBackup?, checkKeyBackup: Bool) {
        self.keyBackup = keyBackup
        self.checkKeyBackup = checkKeyBackup
    }    
}
