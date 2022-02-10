import Foundation

/// Empty view controller used to embed a view conforming to `SlidingModalPresentable`.
final class SlidingModalEmptyViewController: UIViewController {
    
    // MARK: - Properties
    
    private var modalView: SlidingModalPresentable.ViewType!
    
    // MARK: - Setup
    
    static func instantiate(with view: SlidingModalPresentable.ViewType) -> SlidingModalEmptyViewController {
        let viewController = SlidingModalEmptyViewController()
        viewController.modalView = view
        return viewController
    }
    
    // MARK: - Life cycle
    
    override func loadView() {
        self.view = self.modalView
    }
}

// MARK: - SlidingModalPresentable
extension SlidingModalEmptyViewController: SlidingModalPresentable {
    
    func allowsDismissOnBackgroundTap() -> Bool {
        return self.modalView.allowsDismissOnBackgroundTap()
    }
    
    func layoutHeightFittingWidth(_ width: CGFloat) -> CGFloat {
        return self.modalView.layoutHeightFittingWidth(width)
    }
}
