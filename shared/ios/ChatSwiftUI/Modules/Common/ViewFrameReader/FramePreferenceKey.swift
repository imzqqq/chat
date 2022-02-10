import SwiftUI

/// A SwiftUI `PreferenceKey` for `CGRect` values such as a view's frame.
@available(iOS 14.0, *)
struct FramePreferenceKey: PreferenceKey {
    static var defaultValue: CGRect = .zero
    
    static func reduce(value: inout CGRect, nextValue: () -> CGRect) {
        value = nextValue()
    }
}
