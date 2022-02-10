import Foundation

extension MXRecoveryService {
    
    var vc_availability: SecretsRecoveryAvailability {
        guard self.hasRecovery() else {
            return .notAvailable
        }
        let secretsRecoveryMode: SecretsRecoveryMode = self.usePassphrase() ? .passphraseOrKey : .onlyKey
        return .available(secretsRecoveryMode)
    }
}
