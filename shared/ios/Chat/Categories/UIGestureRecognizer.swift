import UIKit

extension UIGestureRecognizer {
    
    func vc_isTouchingInside(view: UIView? = nil) -> Bool {
        guard let view = view ?? self.view else {
            return false
        }
        let touchedLocation = self.location(in: view)
        return view.bounds.contains(touchedLocation)
    }
}
