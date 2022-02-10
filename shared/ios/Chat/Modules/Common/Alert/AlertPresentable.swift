import Foundation

/// AlertPresentable absracts an alert presenter
protocol AlertPresentable {
    
    func showError(_ error: Error, animated: Bool, completion: (() -> Void)?)
    func show(title: String?, message: String?, animated: Bool, completion: (() -> Void)?)
}

// MARK: Default implementation
extension AlertPresentable {
    
    func showError(_ error: Error) {
        self.showError(error, animated: true, completion: nil)
    }
    
    func show(title: String?, message: String?) {
        self.show(title: title, message: message, animated: true, completion: nil)
    }
}
