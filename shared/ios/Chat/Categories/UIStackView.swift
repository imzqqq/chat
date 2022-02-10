import UIKit

extension UIStackView {
    
    func vc_removeAllArrangedSubviews() {
        let subviews = self.arrangedSubviews
        for subview in subviews {
            self.removeArrangedSubview(subview)
            subview.removeFromSuperview()
        }
    }
}
