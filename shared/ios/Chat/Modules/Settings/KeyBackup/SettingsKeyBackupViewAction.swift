import UIKit

enum SettingsKeyBackupViewAction {
    case load
    case create
    case restore(MXKeyBackupVersion)
    case confirmDelete(MXKeyBackupVersion)
    case delete(MXKeyBackupVersion)
}
