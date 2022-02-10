import Foundation

/// `SlidingModalPresentable` is a protocol describing a UI element to present modally using `SlidingModalPresenter`.
@objc protocol SlidingModalPresentable {
    
    typealias ViewType = UIView & SlidingModalPresentable
    
    typealias ViewControllerType = UIViewController & SlidingModalPresentable
    
    func allowsDismissOnBackgroundTap() -> Bool
    
    func layoutHeightFittingWidth(_ width: CGFloat) -> CGFloat
}
