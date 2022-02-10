import UIKit

// Source: https://stackoverflow.com/a/44917661
class ClosureSleeve {
    let closure: () -> Void

    init(attachTo: AnyObject, closure: @escaping () -> Void) {
        self.closure = closure
        objc_setAssociatedObject(attachTo, "[\(arc4random())]", self, .OBJC_ASSOCIATION_RETAIN)
    }

    @objc func invoke() {
        closure()
    }
}

extension UIControl {
    func vc_addAction(for controlEvents: UIControl.Event = .primaryActionTriggered, action: @escaping () -> Void) {
        let sleeve = ClosureSleeve(attachTo: self, closure: action)
        addTarget(sleeve, action: #selector(ClosureSleeve.invoke), for: controlEvents)
    }
}
