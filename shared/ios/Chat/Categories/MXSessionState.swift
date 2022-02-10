import Foundation

extension MXSessionState: Comparable {
    
    public static func < (lhs: MXSessionState, rhs: MXSessionState) -> Bool {
        return lhs.rawValue < rhs.rawValue
    }
    
}
