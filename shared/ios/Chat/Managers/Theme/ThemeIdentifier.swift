import Foundation

enum ThemeIdentifier: String, RawRepresentable {
    case light = "default"
    case dark = "dark"
    case black = "black"
    
    init?(rawValue: String) {
        switch rawValue {
        case "default":
            self = .light
        case "dark":
            self = .dark
        case "black":
            self = .black
        default:
            return nil
        }
    }
}
