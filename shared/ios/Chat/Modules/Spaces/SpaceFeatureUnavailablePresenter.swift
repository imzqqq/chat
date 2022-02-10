import UIKit

/// SpaceFeatureUnavailablePresenter enables to present modals for unavailable space features
@objcMembers
final class SpaceFeatureUnavailablePresenter: NSObject {
    
    // MARK: - Constants
    
    // MARK: - Properties
            
    // MARK: Private
    
    private weak var presentingViewController: UIViewController?
        
    // MARK: - Public
    
    func presentUnavailableFeature(from presentingViewController: UIViewController,
                                   animated: Bool) {
        
        let spaceFeatureUnavailableVC = SpaceFeatureUnaivableViewController.instantiate()
        
        let navigationVC = ChatNavigationController(rootViewController: spaceFeatureUnavailableVC)
        
        spaceFeatureUnavailableVC.navigationItem.rightBarButtonItem = MXKBarButtonItem(title: MatrixKitL10n.ok, style: .plain, action: { [weak navigationVC] in
            navigationVC?.dismiss(animated: true)
        })
                        
        navigationVC.modalPresentationStyle = .formSheet
        presentingViewController.present(navigationVC, animated: animated, completion: nil)
    }
}
