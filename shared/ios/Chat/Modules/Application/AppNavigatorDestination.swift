import Foundation

/// Supported destinations used by AppNavigator to navigate in screen hierarchy
enum AppNavigatorDestination {
    
    /// Show home space
    case homeSpace
        
    /// Show a space with specific id
    case space(_ spaceId: String)
}
