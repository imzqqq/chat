import Foundation
import SwiftUI

/// Used to calculate the frame of a view.
///
/// Useful in situations as with `ZStack` where you might want to layout views using alignment guides.
/// ```
/// @State private var frame: CGRect = CGRect.zero
/// ...
/// SomeView()
///    .background(ViewFrameReader(frame: $frame))
/// ```
@available(iOS 14.0, *)
struct ViewFrameReader: View {
    @Binding var frame: CGRect
    
    var body: some View {
        GeometryReader { geometry in
            Color.clear
                .preference(key: FramePreferenceKey.self,
                            value: geometry.frame(in: .local))
        }
        .onPreferenceChange(FramePreferenceKey.self) {
            frame = $0
        }
    }
}
