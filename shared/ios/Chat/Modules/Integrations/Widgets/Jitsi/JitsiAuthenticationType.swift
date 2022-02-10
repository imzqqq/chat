import Foundation

/// `JitsiAuthenticationType` represents authentification type supported by a Jitsi server
/// See https://github.com/matrix-org/prosody-mod-auth-matrix-user-verification
enum JitsiAuthenticationType: Equatable {
    case openIDTokenJWT
    case other(String)
            
    private enum KnownAuthenticationType: String {
        case openIDTokenJWT = "openidtoken-jwt"
    }
    
    var identifier: String {
        switch self {
        case .openIDTokenJWT:
            return KnownAuthenticationType.openIDTokenJWT.rawValue
        case .other(let authentificationString):
            return authentificationString
        }
    }
}

extension JitsiAuthenticationType {
    init(_ value: String) {
        switch value {
        case KnownAuthenticationType.openIDTokenJWT.rawValue:
            self = .openIDTokenJWT
        default:
            self = .other(value)
        }
    }
}
