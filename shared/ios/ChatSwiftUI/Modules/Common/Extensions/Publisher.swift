import Foundation
import Combine

@available(iOS 14.0, *)
extension Publisher where Failure == Never {
    /// Same as `assign(to:on:)` but maintains a weak reference to object
    ///
    /// Useful in cases where you want to pass self and not cause a retain cycle.
    func weakAssign<T: AnyObject>(
        to keyPath: ReferenceWritableKeyPath<T, Output>,
        on object: T
    ) -> AnyCancellable {
        sink { [weak object] value in
            object?[keyPath: keyPath] = value
        }
    }
}
