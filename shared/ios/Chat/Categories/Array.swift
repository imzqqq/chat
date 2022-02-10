import Foundation

extension Array where Element: Equatable {

/// Remove first collection element that is equal to the given `object`
/// Credits: https://stackoverflow.com/a/45008042
    mutating func vc_removeFirstOccurrence(of object: Element) {
        guard let index = firstIndex(of: object) else {
            return
        }
        remove(at: index)
    }    
}
