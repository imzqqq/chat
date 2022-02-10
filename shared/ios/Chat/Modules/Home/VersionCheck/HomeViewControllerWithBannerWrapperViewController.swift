import Foundation

class HomeViewControllerWithBannerWrapperViewController: MXKActivityHandlingViewController, BannerPresentationProtocol {
    
    @objc let homeViewController: HomeViewController
    private var bannerContainerView: UIView!
    private var stackView: UIStackView!
    
    init(viewController: HomeViewController) {
        self.homeViewController = viewController
        
        super.init(nibName: nil, bundle: nil)
        
        extendedLayoutIncludesOpaqueBars = true
        
        self.tabBarItem.tag = viewController.tabBarItem.tag
        self.tabBarItem.image = viewController.tabBarItem.image
        self.accessibilityLabel = viewController.accessibilityLabel
    }
    
    required init?(coder: NSCoder) {
        fatalError("Not implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        homeViewController.willMove(toParent: self)
        
        view.backgroundColor = .clear
        
        stackView = UIStackView()
        stackView.axis = .vertical
        stackView.distribution = .fill
        stackView.alignment = .fill
        
        view.vc_addSubViewMatchingParent(stackView)

        addChild(homeViewController)
        stackView.addArrangedSubview(homeViewController.view)
        homeViewController.didMove(toParent: self)
    }
    
    // MARK: - BannerPresentationProtocol
    
    func presentBannerView(_ bannerView: UIView, animated: Bool) {
        bannerView.alpha = 0.0
        bannerView.isHidden = true
        self.stackView.insertArrangedSubview(bannerView, at: 0)
        self.stackView.layoutIfNeeded()
        
        UIView.animate(withDuration: (animated ? 0.25 : 0.0)) {
            bannerView.alpha = 1.0
            bannerView.isHidden = false
            self.stackView.layoutIfNeeded()
        }
    }
    
    func dismissBannerView(animated: Bool) {
        guard stackView.arrangedSubviews.count > 1, let bannerView = self.stackView.arrangedSubviews.first else {
            return
        }
        
        UIView.animate(withDuration: (animated ? 0.25 : 0.0)) {
            bannerView.alpha = 0.0
            bannerView.isHidden = true
            self.stackView.layoutIfNeeded()
        } completion: { _ in
            bannerView.removeFromSuperview()
        }
    }
}
