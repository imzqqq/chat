import UIKit

/**
 UIView subclass that ignores touches on itself.
 */
class PassthroughView: UIView {
    public override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
        let hitTarget = super.hitTest(point, with: event)
        
        guard hitTarget == self else {
            return hitTarget
        }
        
        return nil
    }
}
