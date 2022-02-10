import Foundation

/**
 Used to avoid retain cycles by creating a proxy that holds a weak reference to the original object.
 One example of that would be using CADisplayLink, which strongly retains its target, when manually invalidating it is unfeasable.
 */
class WeakTarget: NSObject {
    private(set) weak var target: AnyObject?
    let selector: Selector

    static let triggerSelector = #selector(WeakTarget.handleTick(parameter:))

    init(_ target: AnyObject, selector: Selector) {
        self.target = target
        self.selector = selector
    }

    @objc private func handleTick(parameter: Any) {
        _ = self.target?.perform(self.selector, with: parameter)
    }
}
