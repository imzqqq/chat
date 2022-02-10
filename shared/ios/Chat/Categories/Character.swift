import Foundation

extension Character {
    var vc_unicodeScalarCodePoint: UInt32 {
        return self.unicodeScalars[self.unicodeScalars.startIndex].value
    }
}
