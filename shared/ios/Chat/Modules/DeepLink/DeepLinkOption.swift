import Foundation

/// DeepLinkOption represents deep link paths with their respective parameters
enum DeepLinkOption {
    
    /// Used for SSO callback only when VoiceOver is enabled
    case connect(_ loginToken: String, _ transactionId: String)
}
