import Foundation

enum SSOURLConstants {
    
    enum Parameters {
        static let callbackLoginToken = "loginToken"
        static let redirectURL = "redirectUrl"
    }
    
    enum Paths {
        static let redirect = "/chat/client/r0/login/sso/redirect"
    }
}
