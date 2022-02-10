import Foundation

extension UITouch {
    
    func vc_isInside(view: UIView? = nil) -> Bool {
        guard let view = view ?? self.view else {
            return false
        }
        let touchedLocation = self.location(in: view)
        return view.bounds.contains(touchedLocation)
    }
}
