import Foundation

@objc protocol BannerPresentationProtocol {
    func presentBannerView(_ bannerView: UIView, animated: Bool)
    func dismissBannerView(animated: Bool)
}
