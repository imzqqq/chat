import Foundation

/// Used to handle the application information
@objcMembers
final class AppInfo: NSObject {
 
    // MARK: - Constants
    
    /// Current application information
    static var current: AppInfo {
        let appDisplayName = BuildSettings.bundleDisplayName
        let buildInfo: BuildInfo = BuildInfo()
        
        return AppInfo(displayName: appDisplayName,
                       appVersion: AppVersion.current,
                       buildInfo: buildInfo)
    }
    
    // MARK: - Properties
    
    /// App display name
    let displayName: String
    
    /// Current app version
    let appVersion: AppVersion?
    
    /// Compilation build info
    let buildInfo: BuildInfo
    
    // MARK: - Setup
    
    init(displayName: String,
         appVersion: AppVersion?,
         buildInfo: BuildInfo) {
        self.displayName = displayName
        self.appVersion = appVersion
        self.buildInfo = buildInfo
    }
}
