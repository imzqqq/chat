import CoreGraphics

extension CGRect {
    var containsNaN: Bool {
        origin.x.isNaN || origin.y.isNaN || size.width.isNaN || size.height.isNaN
    }
}
