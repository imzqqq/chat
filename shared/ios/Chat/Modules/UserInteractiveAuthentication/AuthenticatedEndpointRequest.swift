import Foundation

/// AuthenticatedEndpointRequest represents authenticated API endpoint request.
@objcMembers
class AuthenticatedEndpointRequest: NSObject {
    
    let path: String
    let httpMethod: String
    
    init(path: String, httpMethod: String) {
        self.path = path
        self.httpMethod = httpMethod
        super.init()
    }
}
