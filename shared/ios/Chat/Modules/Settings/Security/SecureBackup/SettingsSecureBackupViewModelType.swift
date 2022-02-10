import UIKit

protocol SettingsSecureBackupViewModelViewDelegate: AnyObject {
    func settingsSecureBackupViewModel(_ viewModel: SettingsSecureBackupViewModelType, didUpdateViewState viewState: SettingsSecureBackupViewState)
    func settingsSecureBackupViewModel(_ viewModel: SettingsSecureBackupViewModelType, didUpdateNetworkRequestViewState networkRequestViewSate: SettingsSecureBackupNetworkRequestViewState)
    
    func settingsSecureBackupViewModelShowSecureBackupReset(_ viewModel: SettingsSecureBackupViewModelType)

    func settingsSecureBackupViewModelShowKeyBackupCreate(_ viewModel: SettingsSecureBackupViewModelType)
    func settingsSecureBackupViewModel(_ viewModel: SettingsSecureBackupViewModelType, showKeyBackupRecover keyBackupVersion: MXKeyBackupVersion)
    func settingsSecureBackupViewModel(_ viewModel: SettingsSecureBackupViewModelType, showKeyBackupDeleteConfirm keyBackupVersion: MXKeyBackupVersion)
}

protocol SettingsSecureBackupViewModelType {

    var viewDelegate: SettingsSecureBackupViewModelViewDelegate? { get set }

    func process(viewAction: SettingsSecureBackupViewAction)
}
