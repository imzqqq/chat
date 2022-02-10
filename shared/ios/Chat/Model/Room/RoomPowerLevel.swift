import Foundation

/// Chat Standard Room Member Power Level
@objc
public enum RoomPowerLevel: Int {
    case admin = 100
    case moderator = 50
    case user = 0
    
    public init?(rawValue: Int) {
        switch rawValue {
        case 100...:
            self = .admin
        case 50...99:
            self = .moderator
        default:
            self = .user
        }
    }
}

@objcMembers
public final class RoomPowerLevelHelper: NSObject {
    
    static func roomPowerLevel(from rawValue: Int) -> RoomPowerLevel {
        return RoomPowerLevel(rawValue: rawValue) ?? .user
    }
}
