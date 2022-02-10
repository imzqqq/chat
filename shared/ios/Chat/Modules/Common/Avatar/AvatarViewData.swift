import Foundation

struct AvatarViewData: AvatarViewDataProtocol {
    /// Matrix item identifier (user id or room id)
    var matrixItemId: String
    
    /// Matrix item display name (user or room display name)
    var displayName: String?

    /// Matrix item avatar URL (user or room avatar url)
    var avatarUrl: String?
        
    /// Matrix media handler if exists
    var mediaManager: MXMediaManager?
    
    /// Fallback image used when avatarUrl is nil
    var fallbackImage: AvatarFallbackImage?
}
