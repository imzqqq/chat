import Foundation

/// Represents the homeserver configuration (usually based on HS Well-Known or hardoced values in the project)
@objcMembers
final class HomeserverConfiguration: NSObject {
    
    // Note: Use an object per configuration subject when there is multiple properties related
    let jitsi: HomeserverJitsiConfiguration
    let isE2EEByDefaultEnabled: Bool
    
    init(jitsi: HomeserverJitsiConfiguration,
         isE2EEByDefaultEnabled: Bool) {
        self.jitsi = jitsi
        self.isE2EEByDefaultEnabled = isE2EEByDefaultEnabled
        
        super.init()
    }
}
