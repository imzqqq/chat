import Foundation

class AppAlertPresenter: AlertPresentable {
    
    // MARK: - Properties
    // swiftlint:disable weak_delegate
    private let legacyAppDelegate: LegacyAppDelegate
    // swiftlint:enable weak_delegate
    
    // MARK: - Setup
    init(legacyAppDelegate: LegacyAppDelegate) {
        self.legacyAppDelegate = legacyAppDelegate
    }
    
    // MARK: - Public
    func showError(_ error: Error, animated: Bool, completion: (() -> Void)?) {
        // FIXME: Present an error on coordinator.toPresentable()
        self.legacyAppDelegate.showError(asAlert: error)
    }
    
    func show(title: String?, message: String?, animated: Bool, completion: (() -> Void)?) {
        // FIXME: Present an error on coordinator.toPresentable()
        self.legacyAppDelegate.showAlert(withTitle: title, message: message)
    }
}
