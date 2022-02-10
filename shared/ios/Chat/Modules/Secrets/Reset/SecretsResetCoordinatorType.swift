// File created from ScreenTemplate
// $ createScreen.sh Secrets/Reset SecretsReset

import Foundation

protocol SecretsResetCoordinatorDelegate: AnyObject {
    func secretsResetCoordinatorDidResetSecrets(_ coordinator: SecretsResetCoordinatorType)
    func secretsResetCoordinatorDidCancel(_ coordinator: SecretsResetCoordinatorType)
}

/// `SecretsResetCoordinatorType` is a protocol describing a Coordinator that handle keys reset flow.
protocol SecretsResetCoordinatorType: Coordinator, Presentable {
    var delegate: SecretsResetCoordinatorDelegate? { get }
}
