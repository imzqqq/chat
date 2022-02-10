// File created from ScreenTemplate
// $ createScreen.sh Secrets/Reset SecretsReset

import Foundation

final class SecretsResetViewModel: SecretsResetViewModelType {
    
    // MARK: - Properties
    
    // MARK: Private

    private let session: MXSession
    private let recoveryService: MXRecoveryService
    private let crossSigningService: CrossSigningService
    
    // MARK: Public

    weak var viewDelegate: SecretsResetViewModelViewDelegate?
    weak var coordinatorDelegate: SecretsResetViewModelCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(session: MXSession) {
        self.session = session
        self.recoveryService = session.crypto.recoveryService
        self.crossSigningService = CrossSigningService()
    }
    
    // MARK: - Public
    
    func process(viewAction: SecretsResetViewAction) {
        switch viewAction {
        case .loadData:
            break
        case .reset:
            self.askAuthentication()
        case .authenticationInfoEntered(let authParameters):
            self.resetSecrets(with: authParameters)
        case .cancel:
            self.coordinatorDelegate?.secretsResetViewModelDidCancel(self)
        }
    }
    
    // MARK: - Private
    
    func update(viewState: SecretsResetViewState) {
        self.viewDelegate?.secretsResetViewModel(self, didUpdateViewState: viewState)
    }
    
    private func resetSecrets(with authParameters: [String: Any]) {
        guard let crossSigning = self.session.crypto.crossSigning else {
            return
        }
        MXLog.debug("[SecretsResetViewModel] resetSecrets")

        self.update(viewState: .resetting)
        crossSigning.setup(withAuthParams: authParameters, success: { [weak self] in
            guard let self = self else {
                return
            }
            self.recoveryService.deleteRecovery(withDeleteServicesBackups: true, success: { [weak self] in
                guard let self = self else {
                    return
                }
                self.update(viewState: .resetDone)
                self.coordinatorDelegate?.secretsResetViewModelDidResetSecrets(self)

            }, failure: { [weak self] error in
                guard let self = self else {
                    return
                }
                self.update(viewState: .error(error))
            })

        }, failure: { [weak self] error in
            guard let self = self else {
                return
            }
            self.update(viewState: .error(error))
        })
    }
    
    private func askAuthentication() {
        let setupCrossSigningRequest = self.crossSigningService.setupCrossSigningRequest()
        self.coordinatorDelegate?.secretsResetViewModel(self, needsToAuthenticateWith: setupCrossSigningRequest)
    }
}
