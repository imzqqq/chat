import Foundation

/// Protocol describing the view model used by `SecureBackupSetupIntroViewController`
protocol SecureBackupSetupIntroViewModelType {
            
    // TODO: Hide these properties from interface and use same behavior as other view models
    var keyBackup: MXKeyBackup? { get }
    var checkKeyBackup: Bool { get }
}
