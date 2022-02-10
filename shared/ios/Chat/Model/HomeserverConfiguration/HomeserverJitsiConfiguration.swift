import Foundation

/// `HomeserverJitsiConfiguration` gives Jitsi widget configuration used by homeserver
@objcMembers
final class HomeserverJitsiConfiguration: NSObject {
    let serverDomain: String
    let serverURL: URL
    
    init(serverDomain: String, serverURL: URL) {
        self.serverDomain = serverDomain
        self.serverURL = serverURL
        
        super.init()
    }
}
