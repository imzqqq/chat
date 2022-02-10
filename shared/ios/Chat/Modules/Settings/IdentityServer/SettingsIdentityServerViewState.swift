// File created from ScreenTemplate
// $ createScreen.sh Test SettingsIdentityServer

import Foundation

/// SettingsIdentityServerViewController view state
enum SettingsIdentityServerViewState {
    case loading
    case loaded(displayMode: SettingsIdentityServerDisplayMode)
    case presentTerms(session: MXSession, accessToken: String, baseUrl: String, onComplete: (Bool) -> Void)
    case alert(alert: SettingsIdentityServerAlert, onContinue: () -> Void)
    case error(Error)
}

enum SettingsIdentityServerDisplayMode {
    case noIdentityServer
    case identityServer(host: String)
}

/// Alerts that can be presented when the user triggered an action among SettingsIdentityServerViewAction.
/// These alerts allow interaction with the user to complete the action flow.
enum SettingsIdentityServerAlert {
    case addActionAlert(AddActionAlert)
    case changeActionAlert(ChangeActionAlert)
    case disconnectActionAlert(DisconnectActionAlert)

    enum AddActionAlert {
        case invalidIdentityServer(newHost: String)
        case noTerms(newHost: String)
        case termsNotAccepted(newHost: String)
    }

    enum ChangeActionAlert {
        case invalidIdentityServer(newHost: String)
        case noTerms(newHost: String)
        case termsNotAccepted(newHost: String)
        case stillSharing3Pids(oldHost: String, newHost: String)
        case doubleConfirmation(oldHost: String, newHost: String)
    }

    enum DisconnectActionAlert {
        case stillSharing3Pids(oldHost: String)
        case doubleConfirmation(oldHost: String)
    }
}
