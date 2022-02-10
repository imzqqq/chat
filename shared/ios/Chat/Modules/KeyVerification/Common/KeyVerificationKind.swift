import Foundation

enum KeyVerificationKind {
    case otherSession // An other session
    case thisSession  // My current session is new
    case newSession   // My other session is new
    case user         // Another user
    
    var verificationTitle: String {
        
        let title: String
        
        switch self {
        case .otherSession:
            title = VectorL10n.keyVerificationOtherSessionTitle
        case .thisSession:
            title = VectorL10n.keyVerificationThisSessionTitle
        case .newSession:
            title = VectorL10n.keyVerificationNewSessionTitle
        case .user:
            title = VectorL10n.keyVerificationUserTitle
        }
        
        return title
    }
}
