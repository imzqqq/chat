import Foundation

/// Paswword strength
enum PasswordStrength: UInt {
    
    case tooGuessable
    case veryGuessable
    case somewhatGuessable
    case safelyUnguessable
    case veryUnguessable
}
