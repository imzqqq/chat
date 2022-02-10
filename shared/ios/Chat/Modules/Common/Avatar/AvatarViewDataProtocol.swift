import UIKit

enum AvatarFallbackImage {
    
    /// matrixItem represent a Matrix item like a room, space, user
    /// matrixItemId: Matrix item identifier (user id or room id)
    /// displayName: Matrix item display name (user or room display name)
    case matrixItem(_ matrixItemId: String, _ displayName: String?)
    
    /// Normal image with optional content mode
    case image(_ image: UIImage, _ contentMode: UIView.ContentMode? = nil)
}

/// AvatarViewDataProtocol describe a view data that should be given to an AvatarView sublcass
protocol AvatarViewDataProtocol: AvatarProtocol {
    /// Matrix item identifier (user id or room id)
    var matrixItemId: String { get }
    
    /// Matrix item display name (user or room display name)
    var displayName: String? { get }
    
    /// Matrix item avatar URL (user or room avatar url)
    var avatarUrl: String? { get }            
        
    /// Matrix media handler
    var mediaManager: MXMediaManager? { get }
    
    /// Fallback image used when avatarUrl is nil
    var fallbackImage: AvatarFallbackImage? { get }
}
