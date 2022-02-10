import Foundation

/// `SizingViewHeight` allows to associate a height for a given width to a unique value.
final class SizingViewHeight: Hashable, Equatable, CustomStringConvertible {
    
    // MARK: - Properties
    
    let uniqueIdentifier: Int
    var heights: [CGFloat /* width */: CGFloat /* height */] = [:]
    
    var description: String {
        return "<\(type(of: self))> uniqueIdentifier: \(uniqueIdentifier) - heights: \(heights)"
    }
    
    // MARK: - Setup
    
    init(uniqueIdentifier: Int) {
        self.uniqueIdentifier = uniqueIdentifier
    }
    
    // MARK: - Hashable
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(self.uniqueIdentifier)
    }
    
    // MARK: - Equatable
    
    static func == (lhs: SizingViewHeight, rhs: SizingViewHeight) -> Bool {
        return lhs.uniqueIdentifier == rhs.uniqueIdentifier
    }
}
