import UIKit

enum SettingsSecureBackupViewAction {
    case load
    case createSecureBackup
    case resetSecureBackup
    case createKeyBackup
    case restoreFromKeyBackup(MXKeyBackupVersion)
    case confirmDeleteKeyBackup(MXKeyBackupVersion)
    case deleteKeyBackup(MXKeyBackupVersion)
}
