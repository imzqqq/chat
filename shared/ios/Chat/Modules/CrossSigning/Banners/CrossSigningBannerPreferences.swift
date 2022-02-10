import Foundation

/// Cross-Signing banner user preferences.
@objcMembers
final class CrossSigningBannerPreferences: NSObject {
    
    // MARK: - Constants
    
    private enum UserDefaultsKeys {
        static let hideSetupBanner = "CrossSigningBannerPreferencesHideSetupBanner"
    }
    
    static let shared = CrossSigningBannerPreferences()
    
    // MARK: - Properties
    
    // MARK: - Public
    
    /// Remember to hide key backup setup banner.
    var hideSetupBanner: Bool {
        get {
            return UserDefaults.standard.bool(forKey: UserDefaultsKeys.hideSetupBanner)
        }
        set {
            UserDefaults.standard.set(newValue, forKey: UserDefaultsKeys.hideSetupBanner)
        }
    }
    
    /// Reset key backup banner preferences to default values
    func reset() {
        self.hideSetupBanner = false
    }
}
