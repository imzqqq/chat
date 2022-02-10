// File created from ScreenTemplate
// $ createScreen.sh Test SettingsIdentityServer

import Foundation

protocol SettingsIdentityServerViewModelViewDelegate: AnyObject {
    func settingsIdentityServerViewModel(_ viewModel: SettingsIdentityServerViewModelType, didUpdateViewState viewSate: SettingsIdentityServerViewState)
}

/// Protocol describing the view model used by `SettingsIdentityServerViewController`
protocol SettingsIdentityServerViewModelType {
        
    var viewDelegate: SettingsIdentityServerViewModelViewDelegate? { get set }

    var identityServer: String? { get }
    
    func process(viewAction: SettingsIdentityServerViewAction)
}
